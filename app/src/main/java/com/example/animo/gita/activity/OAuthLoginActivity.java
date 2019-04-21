package com.example.animo.gita.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.Utility;
import com.example.animo.gita.model.Access;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GithubAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 24/5/17.
 */

public class OAuthLoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = OAuthLoginActivity.class.getSimpleName();

    private WebView mWebView;
    private Activity mActivity;
    private FirebaseAuth mAuth;

    public OAuthLoginActivity() {
        this.mActivity = this;
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private AdView mAdView;
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_web_view);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = intent.getStringExtra(Constants.TITLE);
        //Log.d(LOG_TAG,"Title is "+title);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String url = intent.getStringExtra(Constants.URL);

        mWebView = (WebView) findViewById(R.id.file_web_view);
        mWebView.getSettings().setCacheMode(mWebView.getSettings().LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.clearHistory();

        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);

        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d(LOG_TAG, "Cookie removed: " + aBoolean);
                }
            });
        }
        else cookieManager.removeAllCookie();

        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.e(LOG_TAG,"url is "+url);
                String title = mWebView.getTitle();
                //Log.e(LOG_TAG,"Title is "+title);

                /*if(url.startsWith(Constants.REDIRECT_URL)){
                    authenticateUri(url);
                }*/
                //finish();
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //Log.e(LOG_TAG,"onPageStarted url "+url);
                if(url.startsWith(Constants.REDIRECT_URL)){
                    if(url.contains("?code=")){
                        /*Uri uri = Uri.parse(url);
                        String authCode = uri.getQueryParameter("code");
                        Log.e(LOG_TAG,"code "+authCode);
                        Intent result = new Intent();
                        result.putExtra("token",url);
                        setResult(1,result);
                        finish();*/
                        //mWebView.stopLoading();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        ProgressDialog progressBar = new ProgressDialog(mActivity);
                        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressBar.setCancelable(false);
                        progressBar.setMessage("Logging you in.....");
                        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                        //progressBar.setIndeterminate(false);
                        //progressBar.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        progressBar.show();
                        //progressBar.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        authenticateUri(url);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //Log.e(LOG_TAG,"Web request "+request.getUrl().toString());
                if(request.getUrl().toString().startsWith(Constants.REDIRECT_URL))
                    return true;
                //return super.shouldOverrideUrlLoading(view, request);
                return false;
            }

        });


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void authenticateUri(String url) {
        Uri uri = Uri.parse(url);
        if(uri != null && uri.toString().startsWith(Constants.REDIRECT_URL)){
            String code = uri.getQueryParameter(Constants.AUTH_CODE);
            String state = uri.getQueryParameter(Constants.AUTH_STATE);
            //Log.d(LOG_TAG,"Returned State "+state);
            if(!state.equals(Constants.RANDOM_CODE)){
                Log.e(LOG_TAG,"It is a forgery attack ....Stopping");
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
                                               // Log.d(LOG_TAG,"Sign in with credential "+task.isSuccessful());
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

    private void saveToSharedPreference(String key ,String value) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.commit();
    }
}
