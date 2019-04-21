package com.example.animo.gita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.adapter.FilesAdapter;
import com.example.animo.gita.model.Files;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 24/5/17.
 */

public class FilesFragment extends Fragment {

    public static final String LOG_TAG = FilesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FilesAdapter filesAdapter;

    private List<Files> filesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.files_fragment,container,false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.files_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        filesAdapter = new FilesAdapter(getContext());
        mRecyclerView.setAdapter(filesAdapter);

        final String repo = getArguments().getString(Constants.REPO);
        final String owner = getArguments().getString(Constants.OWNER);
        final String path = getArguments().getString(Constants.PATH);

        //Log.d(LOG_TAG,"Repo name "+repo+" and owner "+owner+" and Path "+path);

        ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
        Call<List<Files>> call = apiService.getContents(owner,repo,path);
        call.enqueue(new Callback<List<Files>>() {
            @Override
            public void onResponse(Call<List<Files>> call, Response<List<Files>> response) {
                //Log.i(LOG_TAG,"inside onResponse");
                filesList = response.body();
                filesAdapter.setFilesList(filesList);
                filesAdapter.setRepoName(repo);
                filesAdapter.setOwner(owner);
                filesAdapter.notifyDataSetChanged();
                //Log.d(LOG_TAG,"Size of Commits "+filesList.size());

            }

            @Override
            public void onFailure(Call<List<Files>> call, Throwable t) {
                //Log.e(LOG_TAG,"inside Failure "+t.toString());

            }
        });

        return rootView;
    }
}
