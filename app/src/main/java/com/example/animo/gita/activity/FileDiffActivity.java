package com.example.animo.gita.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;

/**
 * Created by animo on 29/5/17.
 */

public class FileDiffActivity extends AppCompatActivity{
    private static final String LOG_TAG = FileDiffActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_diff);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = intent.getStringExtra(Constants.TITLE);
        //Log.d(LOG_TAG,"Title is "+title);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String content = intent.getStringExtra(Constants.DIFF_CONTENT);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diff_layout);

        String[] splitLines = content.split("\n");
        for(int i=0;i<splitLines.length;i++){
            TextView textView = new TextView(this);
            if(splitLines[i].startsWith("+")){
                textView.setBackgroundColor(Color.parseColor("#ABEBC6"));
            }else if(splitLines[i].startsWith("-"))
                textView.setBackgroundColor(Color.parseColor("#B589A0"));
            else if(splitLines[i].startsWith("@@"))
                textView.setBackgroundColor(Color.parseColor("#9BADC9"));
            textView.setText(splitLines[i]);
            textView.setTypeface(Typeface.MONOSPACE);
            textView.setMaxLines(1);
            textView.setMovementMethod(new ScrollingMovementMethod());
            linearLayout.addView(textView);
        }
    }
}
