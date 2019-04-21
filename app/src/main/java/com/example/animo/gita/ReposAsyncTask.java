package com.example.animo.gita;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.animo.gita.fragments.MainActivityFragment;
import com.example.animo.gita.model.Repository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by animo on 10/5/17.
 */

public class ReposAsyncTask extends AsyncTask<String,Void,List<Repository>> {
    private static final String LOG_TAG = ReposAsyncTask.class.getSimpleName();
    private MainActivityFragment mainActivityFragment;


    public ReposAsyncTask(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    @Override
    protected void onPostExecute(List<Repository> repositories) {
        super.onPostExecute(repositories);
        //batch delete of existing data
        //insert all data
        mainActivityFragment.repoAdapter.setRepositoryList(repositories);
        mainActivityFragment.repoAdapter.notifyDataSetChanged();

    }

    @Override
    protected List<Repository> doInBackground(String... params) {
        //Log.i(LOG_TAG,"inside doInBackground");
        if(params.length == 0)
            return null;
        String accessToken = params[0];
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        String repoJson = null;
        List<Repository> repoList = new ArrayList<>();

        try{
            Uri buildUri = Uri.parse(Constants.ROOT_URL).buildUpon()
                    .appendPath("user")
                    .appendPath("repos")
                    .build();
            URL url = new URL(buildUri.toString());
            //Log.e(LOG_TAG, "url is " + url);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization"," token " + accessToken);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null)
                repoJson = null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0)
                repoJson = null;

            repoJson = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //Log.e(LOG_TAG, "error", e);
                }
            }
        }

        Gson gson = new Gson();
        repoList = gson.fromJson(repoJson, new TypeToken<List<Repository>>(){}.getType());
        //Log.d(LOG_TAG,"Repos size "+repoList.size());
        return repoList;
    }
}
