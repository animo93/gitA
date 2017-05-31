package com.example.animo.gita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.CommitAdapter;
import com.example.animo.gita.model.RepoCommit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 23/5/17.
 */

public class CommitsFragment extends Fragment {

    public static final String LOG_TAG = CommitsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CommitAdapter commitAdapter;

    private List<RepoCommit> repoCommits = new ArrayList<>();

    public CommitsFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_commits,container,false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.commits_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        commitAdapter = new CommitAdapter(getContext());
        mRecyclerView.setAdapter(commitAdapter);

        final String repo = getArguments().getString(Constants.REPO);
        final String owner = getArguments().getString(Constants.OWNER);

        Log.d(LOG_TAG,"Repo name "+repo+" and owner "+owner);

        ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);

        retrofit2.Call<List<RepoCommit>> call = apiService.getCommits(owner,repo);
        call.enqueue(new Callback<List<RepoCommit>>() {
            @Override
            public void onResponse(retrofit2.Call<List<RepoCommit>> call, Response<List<RepoCommit>> response) {
                Log.i(LOG_TAG,"inside onResponse");
                repoCommits = response.body();
                commitAdapter.setRepoCommitList(repoCommits);
                commitAdapter.setOwner(owner);
                commitAdapter.setRepo(repo);
                commitAdapter.notifyDataSetChanged();
                Log.d(LOG_TAG,"Size of Commits "+repoCommits.size());
            }

            @Override
            public void onFailure(retrofit2.Call<List<RepoCommit>> call, Throwable t) {
                Log.e(LOG_TAG,"inside Failure "+t.toString());

            }
        });

        return rootView;

    }
}
