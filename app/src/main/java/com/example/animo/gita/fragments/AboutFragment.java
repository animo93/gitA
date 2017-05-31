package com.example.animo.gita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 26/5/17.
 */

public class AboutFragment extends Fragment {

    public static final String LOG_TAG = AboutFragment.class.getSimpleName();

    public TextView titleView;
    public TextView forkedFromView;
    public TextView descriptionView;
    public TextView languageView;
    public TextView issueCountView;
    public TextView stargazersCountView;
    public TextView forksCountView;
    public WebView webView;

    public List<Files> filesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.repo_about_fragment,container,false);

        initializeViews(rootView);

        setupViews(this);

        final String repo = getArguments().getString(Constants.REPO);
        final String owner = getArguments().getString(Constants.OWNER);

        ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
        Call<List<Files>> call = apiService.getContents(owner,repo,"");
        call.enqueue(new Callback<List<Files>>() {
            @Override
            public void onResponse(Call<List<Files>> call, Response<List<Files>> response) {
                Log.i(LOG_TAG,"inside onResponse");
                filesList = response.body();
                Log.d(LOG_TAG,"Size of Commits "+filesList.size());
                for(Files files : filesList){
                    if(files.getType().equals("file") &&
                            files.getName().equals("README.md")){
                        Log.d(LOG_TAG,"readme url "+files.getDownload_url());
                        webView.loadUrl(files.getDownload_url());
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Files>> call, Throwable t) {
                Log.e(LOG_TAG,"inside Failure "+t.toString());

            }
        });



        return rootView;
    }

    private void setupViews(AboutFragment aboutFragment) {
        titleView.setText(aboutFragment.getArguments().getString(Constants.TITLE));
        String source = aboutFragment.getArguments().getString(Constants.SOURCE);
        if(source == null)
            forkedFromView.setEnabled(false);
        else
            forkedFromView.setText(source);

        String desc = aboutFragment.getArguments().getString(Constants.DESC);
        if(desc == null)
            descriptionView.setEnabled(false);
        else
            descriptionView.setText(desc);

        String language = aboutFragment.getArguments().getString(Constants.LANG);
        if(language == null)
            languageView.setEnabled(false);
        else
            languageView.setText(language);

        Log.d(LOG_TAG,"issues "+aboutFragment.getArguments().getInt(Constants.ISSUE_COUNT));

        issueCountView.setText(String.valueOf(aboutFragment.getArguments().getInt(Constants.ISSUE_COUNT)));
        stargazersCountView.setText(String.valueOf(aboutFragment.getArguments().getInt(Constants.STARGAZERS)));
        forksCountView.setText(String.valueOf(aboutFragment.getArguments().getInt(Constants.FORKS)));

    }

    private void initializeViews(View activity) {
        titleView = (TextView) activity.findViewById(R.id.repo_title);
        forkedFromView = (TextView) activity.findViewById(R.id.forked_from);
        descriptionView = (TextView) activity.findViewById(R.id.description);
        languageView = (TextView) activity.findViewById(R.id.language);
        issueCountView = (TextView) activity.findViewById(R.id.issues);
        stargazersCountView = (TextView) activity.findViewById(R.id.stargazers);
        forksCountView = (TextView) activity.findViewById(R.id.forks);
        webView = (WebView) activity.findViewById(R.id.readme);
    }


}
