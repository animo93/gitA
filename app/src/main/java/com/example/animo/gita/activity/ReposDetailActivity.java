package com.example.animo.gita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.TabsPagerAdapter;
import com.example.animo.gita.fragments.AboutFragment;
import com.example.animo.gita.fragments.CommitsFragment;
import com.example.animo.gita.fragments.FilesFragment;

/**
 * Created by animo on 22/5/17.
 */

public class ReposDetailActivity extends AppCompatActivity{

    public static final String LOG_TAG = ReposDetailActivity.class.getSimpleName();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.i(LOG_TAG,"Inside onSaveInstanceState");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.i(LOG_TAG,"Inside onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repo_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String repo = intent.getStringExtra(Constants.REPO);
        final String owner = intent.getStringExtra(Constants.OWNER);
        final String path = intent.getStringExtra(Constants.PATH);
        final String title = intent.getStringExtra(Constants.TITLE);
        final String sourceId = intent.getStringExtra(Constants.SOURCE);
        //Log.e(LOG_TAG,"source is "+sourceId);
        final String desc = intent.getStringExtra(Constants.DESC);
        final String language = intent.getStringExtra(Constants.LANG);
        final int issueCount = intent.getIntExtra(Constants.ISSUE_COUNT,0);
        final int stargazers = intent.getIntExtra(Constants.STARGAZERS,0);
        final int forksCount = intent.getIntExtra(Constants.FORKS,0);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.REPO,repo);
        bundle.putString(Constants.OWNER,owner);
        bundle.putString(Constants.PATH,path);
        bundle.putString(Constants.TITLE,title);
        bundle.putString(Constants.SOURCE,sourceId);
        bundle.putString(Constants.DESC,desc);
        bundle.putString(Constants.LANG,language);
        bundle.putInt(Constants.ISSUE_COUNT,issueCount);
        bundle.putInt(Constants.STARGAZERS,stargazers);
        bundle.putInt(Constants.FORKS,forksCount);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if(viewPager != null) {
            setupViewPager(viewPager,bundle);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager,Bundle bundle) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        CommitsFragment commitsFragment = new CommitsFragment();
        commitsFragment.setArguments(bundle);

        FilesFragment filesFragment = new FilesFragment();
        filesFragment.setArguments(bundle);

        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.setArguments(bundle);

        adapter.addFragment(filesFragment , "Files");
        adapter.addFragment(commitsFragment ,"Commits");
        adapter.addFragment(aboutFragment, "About");
        viewPager.setAdapter(adapter);
    }
}
