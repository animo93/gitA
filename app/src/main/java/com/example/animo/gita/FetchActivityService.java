package com.example.animo.gita;

import android.database.Cursor;
import android.util.Log;

import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.RepoCommit;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 7/6/17.
 */

public class FetchActivityService extends JobService {

    private static final String LOG_TAG = FetchActivityService.class.getSimpleName();
    private List<String> favRepos = new ArrayList<>();

    public FetchActivityService(){
        favRepos.add("gitA");
    }
    private static final String[] REPO_COLUMNS = {
            RepoContract.FavRepos.TABLE_NAME+ "." + RepoContract.FavRepos._ID,
            RepoContract.FavRepos.COLUMN_TITLE ,
            RepoContract.FavRepos.COLUMN_REPO_ID,
            RepoContract.FavRepos.COLUMN_REPO_OWNER,
            RepoContract.FavRepos.COLUMN_ETAG
    };

    static final int COL_ID=0;
    static final int COL_REPO_TITLE=1;
    static final int COL_REPO_ID=2;
    static final int COL_REPO_OWNER_ID=3;
    static final int COL_ETAG_ID=4;


    @Override
    public boolean onStartJob(JobParameters job) {
        Log.i(LOG_TAG,"Inside onStartJob Job execution started");
        Cursor cursor = getContentResolver().query(RepoContract.FavRepos.CONTENT_URI,
                REPO_COLUMNS,
                null,
                null,
                null);

        if(cursor!=null){
            Log.d(LOG_TAG,"Number of fav repos "+cursor.getCount());
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToFirst()){
                final String owner = cursor.getString(COL_REPO_OWNER_ID);
                String etag = cursor.getString(COL_ETAG_ID);
                final String repo = cursor.getString(COL_REPO_TITLE);

                ApiInterface apiService = ApiClient.createService(ApiInterface.class,null);
                Call<List<Event>> call = apiService.getEventStatus(owner,repo,etag);
                call.enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        Log.i(LOG_TAG,"inside onResponse");
                        if(response !=null){
                            if(response.body()==null)
                                return;
                            else {
                                List<Event> events = response.body();
                                RepoCommit repoCommit = events.get(0).getPayLoad().getCommits().get(0);
                                NotificationService notificationService = new NotificationService(getApplicationContext());
                                notificationService.createIntent(repo,owner,repoCommit.getSha());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Log.e(LOG_TAG,"Failed to make api call"+t);

                    }

                });


            }
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(LOG_TAG,"Inside onStopJob");
        return false;
    }
}
