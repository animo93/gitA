package com.example.animo.gita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by animo on 22/5/17.
 */

public class ReposDetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repo_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String repo = intent.getStringExtra(Constants.REPO);
        final String owner = intent.getStringExtra(Constants.OWNER);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.REPO,repo);
        bundle.putString(Constants.OWNER,owner);

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
        adapter.addFragment(new MainActivityFragment() , "Issues");
        adapter.addFragment(commitsFragment ,"Commits");
        adapter.addFragment(new CommitsFragment() , "Activity");
        viewPager.setAdapter(adapter);
    }
}
