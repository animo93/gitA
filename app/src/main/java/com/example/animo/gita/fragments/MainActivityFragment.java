package com.example.animo.gita.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animo.gita.ReposAsyncTask;
import com.example.animo.gita.Utility;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.RepoAdapter;
import com.example.animo.gita.model.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RepoAdapter repoAdapter;

    private List<Repository> repositories = new ArrayList<>();


    public static final int REPO_LOADER = 4;


    public MainActivityFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(LOG_TAG,"inside onResume ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.e(LOG_TAG,"inside onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repos_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        repoAdapter = new RepoAdapter(getContext());
        mRecyclerView.setAdapter(repoAdapter);

        //Bundle args = getArguments();
        String accessToken = new Utility().getAccessToken(getContext());
        //Log.d(LOG_TAG,"Access Token "+accessToken);

        ReposAsyncTask reposAsyncTask = new ReposAsyncTask(this);
        reposAsyncTask.execute(accessToken);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.e(LOG_TAG,"inside onDestrooyView");
        //call.cancel();
    }
}
