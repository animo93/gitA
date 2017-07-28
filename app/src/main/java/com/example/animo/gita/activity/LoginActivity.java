package com.example.animo.gita.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.model.Access;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 14/6/17.
 */

public class LoginActivity extends AppCompatActivity{

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private Activity mActivity;

    public LoginActivity(){
        Log.i(LOG_TAG,"inside Constructor");
        this.mActivity=this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG,"inside onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"inside onResume");
        Uri uri = getIntent().getData();
        Log.d(LOG_TAG,"uri is "+uri);
        if(uri != null && uri.toString().startsWith(Constants.REDIRECT_URL)){
            String code = uri.getQueryParameter(Constants.AUTH_CODE);
            String state = uri.getQueryParameter(Constants.AUTH_STATE);
            Log.d(LOG_TAG,"Returned State "+state);
            if(!state.equals(Constants.RANDOM_CODE)){
                Log.e(LOG_TAG,"It is a forgery attack ....Stopping");
                return;
            }
            if(code!= null){
                ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
                Call<Access> call = apiService.getAccessToken(code,getResources().getString(R.string.client_id),getResources().getString(R.string.client_secret));
                call.enqueue(new Callback<Access>() {
                    @Override
                    public void onResponse(Call<Access> call, Response<Access> response) {
                        if(response!=null){
                            if(response.body()!=null){
                                Access access = response.body();
                                final String accessToken = access.getAccess_token();
                                AuthCredential credential = GithubAuthProvider.getCredential(accessToken);
                                mAuth.signInWithCredential(credential)
                                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d(LOG_TAG,"Sign in with credential "+task.isSuccessful());
                                                saveToSharedPreference(getString(R.string.access_token),accessToken);
                                                /*Intent intent = new Intent(mActivity,MainActivity.class);
                                                startActivity(intent);*/
                                                finish();
                                                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                                                        getBaseContext().getPackageName()
                                                );
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                if(!task.isSuccessful()){
                                                    Log.e(LOG_TAG,"Sign in with credential ",task.getException());
                                                    Toast.makeText(mActivity,"Authentication Failed",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Access> call, Throwable t) {
                        Log.e(LOG_TAG,"unable to get access token....Try Again"+t.toString());

                    }
                });

            }else {
                Log.e(LOG_TAG,"Unable to authorize ....PLease try again");
            }
        }
    }

    private void saveToSharedPreference(String key ,String value) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d(LOG_TAG,"Current user is "+currentUser.getDisplayName());
            finish();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        Button button = (Button) findViewById(R.id.button_signin);
        Log.d(LOG_TAG,"Random string is "+Constants.RANDOM_CODE);
        final Uri buildUri = Uri.parse(Constants.OATH2_URL).buildUpon()
                .appendQueryParameter("client_id",getResources().getString(R.string.client_id))
                .appendQueryParameter(Constants.AUTH_STATE,Constants.RANDOM_CODE)
                .build();
        Log.d(LOG_TAG,"Final Uri is "+buildUri);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        buildUri
                );
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    public static String random() {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(8);
        for(int i=0;i<8;++i)
            sb.append(Constants.ALLOWED_CHARACTERS.charAt(random.nextInt(Constants.ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
