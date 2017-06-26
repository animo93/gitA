package com.example.animo.gita;

import android.database.Cursor;
import android.util.Log;

import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.PayLoad;
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
    /*private static final String[] REPO_COLUMNS = {
            RepoContract.FavRepos.TABLE_NAME+ "." + RepoContract.FavRepos._ID,
            RepoContract.FavRepos.COLUMN_TITLE ,
            RepoContract.FavRepos.COLUMN_REPO_ID,
            RepoContract.FavRepos.COLUMN_REPO_OWNER,
            RepoContract.FavRepos.COLUMN_ETAG
    };*/




    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(LOG_TAG,"Inside onStartJob Job execution started");
        Cursor cursor = null;
        try{
            cursor = getContentResolver().query(RepoContract.FavRepos.CONTENT_URI,
                    RepoContract.FavRepos.REPO_COLUMNS,
                    null,
                    null,
                    null);

            if(cursor!=null){
                Log.e(LOG_TAG,"Number of fav repos "+cursor.getCount());
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    final String owner = cursor.getString(RepoContract.FavRepos.COL_REPO_OWNER_ID);
                    String etag = "\""+cursor.getString(RepoContract.FavRepos.COL_ETAG_ID)+"\"";
                    final String repo = cursor.getString(RepoContract.FavRepos.COL_REPO_TITLE);
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
                                    if(events!=null && events.size()>0){
                                        Log.i(LOG_TAG,"event size "+events.size());
                                        if(events.get(0).getType().equalsIgnoreCase(Constants.PUSH_EVENT)){
                                            RepoCommit repoCommit = events.get(0).getPayload().getCommits().get(0);
                                            NotificationService notificationService = new NotificationService(getApplicationContext());
                                            notificationService.createIntent(repo,owner,repoCommit.getSha());
                                        }

                                    }

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
        } catch (Exception e){
            Log.e(LOG_TAG,"Cannot start notification "+e);
        } finally {
            cursor.close();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(LOG_TAG,"Inside onStopJob");
        return false;
    }
}
