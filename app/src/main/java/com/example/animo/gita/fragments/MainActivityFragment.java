package com.example.animo.gita.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animo.gita.ReposAsyncTask;
import com.example.animo.gita.Utility;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.AsyncProcessOutput;
import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.RepoAdapter;
import com.example.animo.gita.model.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;

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
    String accessToken = null;


    public MainActivityFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(LOG_TAG,"inside onResume ");
        String accessToken = new Utility().getAccessToken(getContext());
        Log.e(LOG_TAG,"Access Token in frag "+accessToken);


/*        ApiInterface apiService = ApiClient.createService(ApiInterface.class, accessToken);

        retrofit2.Call<List<Repository>> call = apiService.getRepos();
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
            });*/



    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(LOG_TAG,"inside onActivityCreated");
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(REPO_LOADER);
        if(loader==null){
            loaderManager.initLoader(REPO_LOADER, null, this);
        }else{
            loaderManager.restartLoader(REPO_LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }*/

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

        //Bundle args = getArguments();
        String accessToken = new Utility().getAccessToken(getContext());
        Log.d(LOG_TAG,"Access Token "+accessToken);

        ReposAsyncTask reposAsyncTask = new ReposAsyncTask(this);
        reposAsyncTask.execute(accessToken);
       /* String accessToken = new Utility().getAccessToken(getContext());
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
*/

       //getActivity().getSupportLoaderManager().initLoader(REPO_LOADER,null,this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(LOG_TAG,"inside onDestrooyView");
        //call.cancel();
    }

/*    @Override
    public Loader<List<Repository>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"inside onCreateLoader");
        return new AsyncTaskLoader<List<Repository>>(getActivity()) {
            @Override
            protected void onStopLoading() {
                Log.e(LOG_TAG,"inside onStopLoading");
                super.onStopLoading();
                cancelLoad();
            }

            @Override
            protected void onStartLoading() {
                Log.e(LOG_TAG,"inside onStartLoading");
                super.onStartLoading();
                if(call == null)
                    forceLoad();

            }

            @Override
            public List<Repository> loadInBackground() {
                String accessToken = new Utility().getAccessToken(getContext());
                Log.e(LOG_TAG,"Access Token in frag "+accessToken);
                ApiInterface apiService = ApiClient.createService(ApiInterface.class, accessToken);
                call = apiService.getRepos();
                try {
                    Response<List<Repository>> response=call.execute();
                    repositories = response.body();
                    if (repositories != null) {
                        Log.d(LOG_TAG, "Size of repos " + repositories.size());
                        *//*repoAdapter.setRepositoryList(repositories);
                        repoAdapter.notifyDataSetChanged();*//*
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return repositories;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Repository>> loader, List<Repository> data) {
        if(data!=null){
            Log.d(LOG_TAG,"Size of data "+data.size());
            repoAdapter.setRepositoryList(data);
            repoAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Repository>> loader) {


    }*/
}
