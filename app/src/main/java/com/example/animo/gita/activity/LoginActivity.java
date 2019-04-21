package com.example.animo.gita.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.MyApiInterface;
import com.example.animo.gita.MyApiResponse;
import com.example.animo.gita.MyCall;
import com.example.animo.gita.MyCallBack;
import com.example.animo.gita.R;
import com.example.animo.gita.Utility;
import com.example.animo.gita.model.Access;
import com.example.animo.gita.model.DeviceRegisterOutput;
import com.example.animo.gita.model.RepoRegister;
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
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ProgressDialog progressBar;

    private static final int RC_SIGN_IN = 1;

    public LoginActivity(){
       // Log.i(LOG_TAG,"inside Constructor");
        this.mActivity=this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.e(LOG_TAG,"inside onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e(LOG_TAG,"inside onResume");
        mAuth.addAuthStateListener(mAuthStateListener);
        Uri uri = getIntent().getData();
        //Log.e(LOG_TAG,"Uri is "+uri);
        if(uri!=null){
            authenticateUri(uri);
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
           // Log.d(LOG_TAG,"Current user is "+currentUser.getDisplayName());
            finish();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressDialog(mActivity);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setMessage("Connecting to Server.....");
        //progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser!=null){
                    //Log.d(LOG_TAG,"Current user is "+currentUser.getDisplayName());
                    finish();
                    Intent intent = new Intent(mActivity,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
        setContentView(R.layout.activity_signin);
        Button button = (Button) findViewById(R.id.button_signin);
        //Log.d(LOG_TAG,"Random string is "+Constants.RANDOM_CODE);
        /*final Uri buildUri = Uri.parse(Constants.OATH2_URL).buildUpon()
                .appendQueryParameter("client_id",getResources().getString(R.string.client_id))
                .appendQueryParameter("scope","repo admin:repo_hook user")
                .appendQueryParameter(Constants.AUTH_STATE,Constants.RANDOM_CODE)
                .build();*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 0. Start progress bar loading
                   1. Check whether internet connection is available or not
                   2. If client key,id and Random key is present end progress bar loading
                   2. If available , Check for firebase instance id generation
                   3. If id generated , Connect to backend server and fetch client key , client id and Random Code
                   4. End Progress bar loading
                 */
                progressBar.show();
                if(new Utility().isInternetAvailable(mActivity)){
                    //Check if keys are available
                    if(!keysAvailable()){
                        String instanceId=null;
                        int retryCount = Constants.RETRY_COUNT;
                        while(retryCount>0){
                            instanceId = new Utility().getRegToken(mActivity);

                            if(instanceId!=null)
                                break;
                            --retryCount;
                        }
                        if(instanceId!=null){
                            //Connect to backend and fetch client id
                            MyApiInterface apiService = MyApiResponse.createApi(MyApiInterface.class);
                            MyCall<RepoRegister, DeviceRegisterOutput> myCall = apiService.fetchKeys(null,Constants.NOTIF_ROOT_URL+Constants.FETCH_KEYS,new RepoRegister(instanceId,null));
                            myCall.callMeNow(new MyCallBack<RepoRegister, DeviceRegisterOutput>() {
                                @Override
                                public void callBackOnSuccess(MyCall<RepoRegister, DeviceRegisterOutput> myCall) {
                                    if(myCall.getResponseCode()==200){
                                        DeviceRegisterOutput output = myCall.getResponseBody();
                                       // Log.e(LOG_TAG,"Client id= "+output.getClientId()+" and key "+output.getClientKey());
                                        saveToSharedPreference("client_id",output.getClientId());
                                        saveToSharedPreference("client_secret",output.getClientKey());
                                        progressBar.hide();

                                        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                                        intentBuilder.setStartAnimations(mActivity,R.anim.enter_from_right,R.anim.exit_to_left);
                                        CustomTabsIntent customTabsIntent = intentBuilder.build();
                                        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        customTabsIntent.launchUrl(mActivity,buildOAuthUri());

                                        /*Intent intent = new Intent(mActivity,OAuthLoginActivity.class);
                                        intent.putExtra(Constants.URL,buildOAuthUri());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        finish();*/
                                    }else{
                                        progressBar.setMessage("Can't connect to Server");
                                        progressBar.setCancelable(true);
                                    }
                                }

                                @Override
                                public void callBackOnFailure(Exception e) {
                                    progressBar.setMessage("Can't Connect to Server");
                                    progressBar.setCancelable(true);

                                }
                            });

                        }else{
                            progressBar.setMessage("Can't Connect to Server");
                            progressBar.setCancelable(true);
                        }
                    }else{
                        progressBar.cancel();
                        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = intentBuilder.build();
                        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //customTabsIntent.intent.setPackage("com.android.chrome");
                        customTabsIntent.launchUrl(mActivity,buildOAuthUri());

                        /*Intent intent = new Intent(mActivity,OAuthLoginActivity.class);
                        intent.putExtra(Constants.URL,buildOAuthUri());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();*/
                    }

                }else{
                    progressBar.setMessage("No Internet Connection Found");
                    progressBar.setCancelable(true);
                }
            }
        });
    }

    private Uri buildOAuthUri() {
        Uri buildUri = Uri.parse(Constants.OATH2_URL).buildUpon()
                .appendQueryParameter("client_id",new Utility().getClientId(mActivity))
                .appendQueryParameter("scope","repo admin:repo_hook user")
                .appendQueryParameter(Constants.AUTH_STATE,Constants.RANDOM_CODE)
                .build();
        return buildUri;

    }

    private boolean keysAvailable() {
        String clientId = new Utility().getClientId(mActivity);
        String clientKey = new Utility().getClientSecret(mActivity);
        if(clientId!= null && clientKey!=null){
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        //Log.i(LOG_TAG,"inside finish");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i(LOG_TAG,"inside onActivityResult");
        //Log.d(LOG_TAG,"request code "+requestCode+" resultCode "+resultCode+"data "+data.getStringExtra("token"));
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
           // Log.d(LOG_TAG,"Current user is "+currentUser.getDisplayName());
            finish();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            authenticateUri(data.getData());
        }
    }

    private void authenticateUri(Uri uri) {
        if(uri != null && uri.toString().startsWith(Constants.REDIRECT_URL)){
            String code = uri.getQueryParameter(Constants.AUTH_CODE);
            String state = uri.getQueryParameter(Constants.AUTH_STATE);
            //Log.d(LOG_TAG,"Returned State "+state);
            if(!state.equals(Constants.RANDOM_CODE)){
               // Log.e(LOG_TAG,"It is a forgery attack ....Stopping");
                return;
            }
            if(code!= null){
                ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
                String clientId = new Utility().getClientId(mActivity);
                String clientKey = new Utility().getClientSecret(mActivity);
                //Log.e(LOG_TAG,"Client id= "+clientId+" and key "+clientKey);
                Call<Access> call = apiService.getAccessToken(code,clientId,clientKey);
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
                                                //Log.d(LOG_TAG,"Sign in with credential "+task.isSuccessful());
                                                saveToSharedPreference(getString(R.string.access_token),accessToken);
                                                Intent intent = new Intent(mActivity,MainActivity.class);
                                                intent.putExtra("access_token",accessToken);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                                if(!task.isSuccessful()){
                                                    //Log.e(LOG_TAG,"Sign in with credential ",task.getException());
                                                    Toast.makeText(mActivity,getResources().getString(R.string.Login_failure),Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Access> call, Throwable t) {
                        //Log.e(LOG_TAG,"unable to get access token....Try Again"+t.toString());

                    }
                });

            }else {
                //Log.e(LOG_TAG,"Unable to authorize ....PLease try again");
            }
        }
    }
}
