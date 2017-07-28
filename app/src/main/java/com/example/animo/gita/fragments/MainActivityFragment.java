package com.example.animo.gita.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animo.gita.Utility;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.AsyncProcessOutput;
import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.RepoAdapter;
import com.example.animo.gita.model.Repository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncProcessOutput {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RepoAdapter repoAdapter;

    private List<Repository> repositories = new ArrayList<>();

    retrofit2.Call<List<Repository>> call = null;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG,"inside onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repos_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        repoAdapter = new RepoAdapter(getContext());
        mRecyclerView.setAdapter(repoAdapter);
        String accessToken = new Utility().getAccessToken(getContext());
        Log.e(LOG_TAG,"Access Token in frag "+accessToken);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class, accessToken);

        call = apiService.getRepos();
        synchronized (call) {
            call.enqueue(new Callback<List<Repository>>() {
                @Override
                public void onResponse(retrofit2.Call<List<Repository>> call, Response<List<Repository>> response) {
                    Log.e(LOG_TAG, "inside onResponse");
                    repositories = response.body();
                    if (repositories != null) {
                        Log.d(LOG_TAG, "Size of repos " + repositories.size());
                        repoAdapter.setRepositoryList(repositories);
                        repoAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onFailure(retrofit2.Call<List<Repository>> call, Throwable t) {
                    Log.e(LOG_TAG, "inside Failure " + t.toString());

                }
            });
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(LOG_TAG,"inside onDestrooyView");
        call.cancel();
    }

    @Override
    public void onComplete(Object output) {

    }
}
