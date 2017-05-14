package com.example.animo.gita;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncProcessOutput{

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RepoAdapter repoAdapter;

    private List<Repository> repositories = new ArrayList<>();


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repos_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        repoAdapter = new RepoAdapter(getContext());
        mRecyclerView.setAdapter(repoAdapter);

        ApiInterface apiService = ApiClient.createService(ApiInterface.class);

        retrofit2.Call<List<Repository>> call = apiService.getRepos();
        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Repository>> call, Response<List<Repository>> response) {
                Log.i(LOG_TAG,"inside onResponse");
                repositories = response.body();
                repoAdapter.setRepositoryList(repositories);
                repoAdapter.notifyDataSetChanged();
                Log.d(LOG_TAG,"Size of repos "+repositories.size());
            }

            @Override
            public void onFailure(retrofit2.Call<List<Repository>> call, Throwable t) {
                Log.e(LOG_TAG,"inside Failure "+t.toString());

            }
        });

        return rootView;
    }

    @Override
    public void onComplete(Object output) {

    }
}
