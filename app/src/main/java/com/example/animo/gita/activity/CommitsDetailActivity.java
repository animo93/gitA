package com.example.animo.gita.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.CustomViews.ScrollDisabledListView;
import com.example.animo.gita.NotificationService;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.CommitDetailAdapter;
import com.example.animo.gita.adapter.CommitFileAdapter;
import com.example.animo.gita.model.Commit;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 29/5/17.
 */

public class CommitsDetailActivity extends AppCompatActivity {

    private TextView commiterView;
    private TextView daysView;
    private TextView commitName;
    private TextView commitDesc;
    private ScrollDisabledListView changedFilesListView;
    private ScrollDisabledListView addedFilesListView;
    private ScrollDisabledListView renamedFilesListView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommitDetailAdapter commitDetailAdapter;

    private Context mContext;

    public CommitsDetailActivity() {
        this.mContext=this;
    }

    public static final String LOG_TAG = CommitsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_detail_view_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //initializeView();
        mRecyclerView = (RecyclerView) findViewById(R.id.commit_detail_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        commitDetailAdapter = new CommitDetailAdapter(this);
        mRecyclerView.setAdapter(commitDetailAdapter);



        Intent intent = getIntent();
        final String repo = intent.getStringExtra(Constants.REPO);
        final String owner = intent.getStringExtra(Constants.OWNER);
        final String sha = intent.getStringExtra(Constants.SHA);


        ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
        Call<Commit> call = apiService.getCommit(owner,repo,sha);
        call.enqueue(new Callback<Commit>() {
            @Override
            public void onResponse(Call<Commit> call, Response<Commit> response) {
                Log.i(LOG_TAG,"inside onResponse");
                Commit commit = response.body();
                //setupViews(commit);

                getViewTypeMap(commit);
            }

            private void getViewTypeMap(Commit commit) {
                Map<String,List<Files>> fileListMap = new LinkedHashMap<String, List<Files>>();
                List<Files> addedFilesList = new ArrayList<Files>();
                List<Files> renamedFilesList = new ArrayList<Files>();
                List<Files> deletedFilesList = new ArrayList<Files>();

                for(Files files : commit.getFiles()){
                    if(files.getStatus().equals(Constants.FILES_ADDED))
                        addedFilesList.add(files);
                    else if(files.getStatus().equals(Constants.FILES_RENAMED))
                        renamedFilesList.add(files);
                    else deletedFilesList.add(files);
                }
                if(addedFilesList.size()>0)
                    fileListMap.put(Constants.FILES_ADDED,addedFilesList);
                if(renamedFilesList.size()>0)
                fileListMap.put(Constants.FILES_RENAMED,renamedFilesList);
                if(deletedFilesList.size()>0)
                fileListMap.put(Constants.FILES_DELETD,deletedFilesList);
                Log.d(LOG_TAG,"Total map size "+fileListMap.size());

                commitDetailAdapter.setMapFileList(fileListMap);
                commitDetailAdapter.setCommit(commit);
                commitDetailAdapter.notifyDataSetChanged();

            }

            private void setupViews(Commit commit) {
                commiterView.setText(commit.getCommitter().getLogin());
                commitName.setText(commit.getMessage());
                /*commitDesc.setText("Showing "+commit.getFiles().size()+" changed files with "+
                        commit.getStats().getAdditions()+" additions and "+commit.getStats().getDeletions()+" deletions");*/
                commitDesc.setText(getResources().getString(R.string.string_commitSummary,commit.getFiles().size(),commit.getStats().getAdditions(),commit.getStats().getDeletions()));

                setupListViews("added",addedFilesListView,commit);
                setupListViews("renamed",renamedFilesListView,commit);
                setupListViews("modified",changedFilesListView,commit);

            }

            private void setupListViews(String type, ListView listView, Commit commit) {
                List<Files> filesList = new ArrayList<Files>();
                Log.d(LOG_TAG,"Total file list size "+commit.getFiles().size());
                for(Files files : commit.getFiles()){
                    if(files.getStatus().equals(type)) {
                        /*TextView textView = new TextView(mContext);
                        textView.setText(files.getName());

                        TextView stats = new TextView(mContext);
                        String text = "<font color='green'>+"+files.getAdditions()+
                                "</font>   <font color='red'>-"+files.getDeletions()+"</font>.";
                        stats.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                        LinearLayout linearLayout = new LinearLayout(mContext);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.addView(textView);
                        linearLayout.addView(stats);

                        listView.addView(linearLayout);*/
                        filesList.add(files);

                    }
                }
                Log.d(LOG_TAG,"File size of "+type+" is "+filesList.size());
                CommitFileAdapter adapter = new CommitFileAdapter(getApplicationContext(),listView.getId(),filesList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Commit> call, Throwable t) {
                Log.e(LOG_TAG,"inside Failure "+t.toString());
            }
        });
    }


    private void initializeView() {
        /*commiterView = (TextView)findViewById(R.id.commiter_name);
        daysView = (TextView) findViewById(R.id.days);
        commitName = (TextView) findViewById(R.id.commit_name);
        commitDesc = (TextView) findViewById(R.id.commit_desc);
        changedFilesListView = (ScrollDisabledListView) findViewById(R.id.changed_files_list);
        addedFilesListView = (ScrollDisabledListView) findViewById(R.id.added_files_list);
        renamedFilesListView = (ScrollDisabledListView) findViewById(R.id.renamed_files_list);*/
    }
}
