package com.example.animo.gita.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animo.gita.R;

/**
 * Created by animo on 29/5/17.
 */

public class FileDiffActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_diff);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diff_layout);

        String string = "@@ -12,7 +12,7 @@\n android:supportsRtl=\"true\"\n android:theme=\"@style/AppTheme\">\n <activity\n- android:name=\".MainActivity\"\n+ android:name=\".activity.MainActivity\"\n android:label=\"@string/app_name\"\n android:theme=\"@style/AppTheme.NoActionBar\">\n <intent-filter>\n@@ -21,6 +21,24 @@\n <category android:name=\"android.intent.category.LAUNCHER\" />\n </intent-filter>\n </activity>\n+\n+ <activity\n+ android:name=\".activity.ReposDetailActivity\"\n+ android:label=\"@string/app_name\"\n+ android:theme=\"@style/AppTheme.NoActionBar\">\n+ <meta-data\n+ android:name=\"android.support.PARENT_ACTIVITY\"\n+ android:value=\"com.example.animo.gita.activity.MainActivity\" />\n+ </activity>\n+\n+ <activity\n+ android:name=\".activity.FileViewActivity\"\n+ android:label=\"@string/app_name\"\n+ android:theme=\"@style/AppTheme.NoActionBar\">\n+ <meta-data\n+ android:name=\"android.support.PARENT_ACTIVITY\"\n+ android:value=\"com.example.animo.gita.activity.ReposDetailActivity\" />\n+ </activity>\n </application>\n \n </manifest>\n\\ No newline at end of file";
        String[] splitLines = string.split("\n");
        for(int i=0;i<splitLines.length;i++){
            TextView textView = new TextView(this);
            textView.setText(splitLines[i]);
            linearLayout.addView(textView);
        }
    }
}
