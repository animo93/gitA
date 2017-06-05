package com.example.animo.gita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;

/**
 * Created by animo on 29/5/17.
 */

public class FileDiffActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_diff);
        Intent intent = getIntent();
        String content = intent.getStringExtra(Constants.DIFF_CONTENT);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diff_layout);

        String string = "@@ -12,7 +12,7 @@\n android:supportsRtl=\"true\"\n android:theme=\"@style/AppTheme\">\n <activity\n- android:name=\".MainActivity\"\n+ android:name=\".activity.MainActivity\"\n android:label=\"@string/app_name\"\n android:theme=\"@style/AppTheme.NoActionBar\">\n <intent-filter>\n@@ -21,6 +21,24 @@\n <category android:name=\"android.intent.category.LAUNCHER\" />\n </intent-filter>\n </activity>\n+\n+ <activity\n+ android:name=\".activity.ReposDetailActivity\"\n+ android:label=\"@string/app_name\"\n+ android:theme=\"@style/AppTheme.NoActionBar\">\n+ <meta-data\n+ android:name=\"android.support.PARENT_ACTIVITY\"\n+ android:value=\"com.example.animo.gita.activity.MainActivity\" />\n+ </activity>\n+\n+ <activity\n+ android:name=\".activity.FileViewActivity\"\n+ android:label=\"@string/app_name\"\n+ android:theme=\"@style/AppTheme.NoActionBar\">\n+ <meta-data\n+ android:name=\"android.support.PARENT_ACTIVITY\"\n+ android:value=\"com.example.animo.gita.activity.ReposDetailActivity\" />\n+ </activity>\n </application>\n \n </manifest>\n\\ No newline at end of file";
        String[] splitLines = content.split("\n");
        for(int i=0;i<splitLines.length;i++){
            TextView textView = new TextView(this);
            textView.setText(splitLines[i]);
            //textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT ));
            textView.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setHorizontallyScrolling(true);
            linearLayout.addView(textView);
        }
    }
}
