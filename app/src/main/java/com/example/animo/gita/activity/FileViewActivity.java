package com.example.animo.gita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by animo on 10/11/17.
 */

public class FileViewActivity extends AppCompatActivity {
    private static final String LOG_TAG = FileViewActivity.class.getSimpleName();

    private WebView mWebView;

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
        Log.d(LOG_TAG, "Title is " + title);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = intent.getStringExtra(Constants.URL);

        mWebView = (WebView) findViewById(R.id.file_web_view);
        mWebView.loadUrl(url);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
