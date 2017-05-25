package com.example.animo.gita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;

/**
 * Created by animo on 24/5/17.
 */

public class FileViewActivity extends AppCompatActivity {

    private WebView mWebView;

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
        String url = intent.getStringExtra(Constants.URL);

        mWebView = (WebView) findViewById(R.id.file_web_view);
        mWebView.loadUrl(url);

    }
}
