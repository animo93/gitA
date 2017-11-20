package com.example.animo.gita.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.Utility;
import com.example.animo.gita.activity.ReposDetailActivity;
import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.RepoRegister;
import com.example.animo.gita.model.RepoRegisterOutput;
import com.example.animo.gita.model.Repository;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.retrofit.NotificationClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 8/5/17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewHolder> {

    private static final String LOG_TAG = RepoAdapter.class.getSimpleName();

    private List<Repository> repositoryList;

    private Context mContext;

    Repository repository;

    String sourceId;

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
    }


    public RepoAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RepoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.repository,
                    parent,
                    false
            );
            view.setFocusable(true);
            return new RepoAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(RepoAdapterViewHolder holder, final int position) {

        holder.titleView.setText(repositoryList.get(position).getName());
        holder.forkView.setText(String.valueOf(repositoryList.get(position).getForks()));
        holder.languageView.setText(repositoryList.get(position).getLanguage());
        holder.urlView.setText(repositoryList.get(position).getUrl());

        holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Repository repo = repositoryList.get(position);
                Cursor repoCursor = view.getContext().getContentResolver().query(
                        RepoContract.FavRepos.buildRepoUriFromId(String.valueOf(repo.getId())),
                        new String[]{RepoContract.FavRepos.TABLE_NAME+"."+RepoContract.FavRepos._ID},
                        null,
                        null,
                        null
                );
                Log.d(LOG_TAG,"Uri is "+RepoContract.FavRepos.buildRepoUriFromId(String.valueOf(repo.getId())).toString());
                if(repoCursor.moveToFirst()){
                    String text=Constants.REPO_ALREADY_MARKED;
                    int duration=Toast.LENGTH_SHORT;
                    Toast toast=Toast.makeText(view.getContext(),text,duration);
                    toast.show();
                    return;
                }
                ApiInterface apiService = NotificationClient.createService(ApiInterface.class,null);
                Log.d(LOG_TAG,"Onwer "+repo.getOwner().getLogin()+" and repo "+repo.getName());
                //Call<List<Event>> call = apiService.getEventTag(repo.getOwner().getLogin(),repo.getName());

                Call<RepoRegisterOutput> call = apiService.registerFavRepo(new RepoRegister(new Utility().getRegToken(mContext),repositoryList.get(position).getId().toString()));
                call.enqueue(new Callback<RepoRegisterOutput>() {
                    @Override
                    public void onResponse(Call<RepoRegisterOutput> call, Response<RepoRegisterOutput> response) {
                        Log.i(LOG_TAG,"inside onResponse");
                        /*String eTag = response.headers().get(Constants.ETAG);
                        eTag = eTag.split("\"")[1];*/
                        //String lastModified = response.headers().get(Constants.LAST_MODIFIED);
                        //Log.d(LOG_TAG,"eTag of repo "+repo.getName()+" is "+eTag+" and last modified "+lastModified);
                        ContentValues repoValues = new ContentValues();
                        //repoValues.put(RepoContract.FavRepos.COLUMN_ETAG,eTag);
                        repoValues.put(RepoContract.FavRepos.COLUMN_REPO_ID,repo.getId());
                        repoValues.put(RepoContract.FavRepos.COLUMN_REPO_OWNER,repo.getOwner().getLogin());
                        repoValues.put(RepoContract.FavRepos.COLUMN_TITLE,repo.getName());
                        //repoValues.put(RepoContract.FavRepos.COLUMN_UPDATED_DATE,lastModified);

                        Uri insertUri=view.getContext().getContentResolver().insert(RepoContract.FavRepos.CONTENT_URI,repoValues);
                        if(insertUri!=null && response.code() == 200){
                            Log.d(LOG_TAG,"Repo inserted and Uri is" +insertUri.toString());
                            String text=Constants.MARK_REPO_FAVOURITE;
                            int duration=Toast.LENGTH_SHORT;
                            Toast toast=Toast.makeText(view.getContext(),text,duration);
                            toast.show();
                        }


                    }

                    @Override
                    public void onFailure(Call<RepoRegisterOutput> call, Throwable t) {
                        Log.e(LOG_TAG,"Could not insert favourite repos ");
                        t.printStackTrace();

                    }
                });

            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(),ReposDetailActivity.class);
                Repository repo = getRepositoryList().get(position);
                intent.putExtra(Constants.REPO,repo.getName());
                intent.putExtra(Constants.OWNER,repo.getOwner().getLogin());
                intent.putExtra(Constants.TITLE,repo.getFullName());
                intent.putExtra(Constants.DESC, repo.getDescription() == null ?
                        null : (String) repo.getDescription());
                intent.putExtra(Constants.LANG, repo.getLanguage() == null ?
                        null : repo.getLanguage());
                intent.putExtra(Constants.ISSUE_COUNT, repo.getOpenIssuesCount());
                intent.putExtra(Constants.STARGAZERS, repo.getStargazersCount());
                intent.putExtra(Constants.FORKS, repo.getForksCount());
                intent.putExtra(Constants.PATH,"");


                ApiInterface apiService = ApiClient.createService(ApiInterface.class, null);
                Call<Repository> call = apiService.getRepoDetail(repo.getOwner().getLogin(),repo.getName());

                call.enqueue(new Callback<Repository>() {
                    @Override
                    public void onResponse(Call<Repository> call, Response<Repository> response) {
                        repository = response.body();
                        if(repository!=null){
                            sourceId = (repository.getFork()) ? repository.getSource().getOwner().getLogin()
                                    : null;
                            intent.putExtra(Constants.SOURCE,sourceId);
                            mContext.startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<Repository> call, Throwable t) {
                        Log.e(LOG_TAG,"inside failure "+t.toString());

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return repositoryList==null?0:repositoryList.size();
    }

    public class RepoAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView urlView;
        public final TextView forkView;
        public final TextView languageView;
        public final Button favouriteButton;
        public final View mView;


        public RepoAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.title);
            urlView = (TextView) itemView.findViewById(R.id.url);
            forkView = (TextView) itemView.findViewById(R.id.forks);
            languageView = (TextView) itemView.findViewById(R.id.language);
            favouriteButton = (Button) itemView.findViewById(R.id.fav_button);
        }

    }
}
