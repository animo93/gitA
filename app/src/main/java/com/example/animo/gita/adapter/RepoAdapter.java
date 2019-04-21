package com.example.animo.gita.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.activity.MainActivity;
import com.example.animo.gita.activity.ReposDetailActivity;
import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.fragments.MarkFavouriteDialogFragment;
import com.example.animo.gita.fragments.UnmarkFavouriteDialogFragment;
import com.example.animo.gita.model.Config;
import com.example.animo.gita.model.Repository;
import com.example.animo.gita.model.WebHookRegister;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 8/5/17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewHolder>
        {

    private static final String LOG_TAG = RepoAdapter.class.getSimpleName();

    private List<Repository> repositoryList;

    private Context mContext;

    Repository repository;

    String sourceId;

    CompoundButton mCompoundButton;

    int mPosition;

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
        boolean isChecked = false;

        Cursor repoCursor = holder.mView.getContext().getContentResolver().query(
                RepoContract.FavRepos.buildRepoUriFromId(String.valueOf(repositoryList.get(position).getId())),
                new String[]{RepoContract.FavRepos.TABLE_NAME+"."+RepoContract.FavRepos._ID},
                null,
                null,
                null
        );
        if(repoCursor.moveToFirst()){
            isChecked=true;
        }
        holder.favouriteButton.setChecked(isChecked);

        holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition = position;
                mCompoundButton = (CompoundButton) view;

                if(!mCompoundButton.isChecked()){
                    UnmarkFavouriteDialogFragment dialogFragment = new UnmarkFavouriteDialogFragment();
                    String repoJson = new Gson().toJson(repositoryList.get(position));
                    Bundle bundle = new Bundle();
                    bundle.putString("repoJson",repoJson);
                    dialogFragment.setArguments(bundle);
                    MainActivity mainActivity = (MainActivity) mContext;
                    dialogFragment.show(mainActivity.getSupportFragmentManager(),"unmark_favourite_dialog");

                }else{
                    MarkFavouriteDialogFragment dialogFragment = new MarkFavouriteDialogFragment();
                    String repoJson = new Gson().toJson(repositoryList.get(position));
                    Bundle bundle = new Bundle();
                    bundle.putString("repoJson",repoJson);
                    dialogFragment.setArguments(bundle);
                    MainActivity mainActivity = (MainActivity) mContext;
                    dialogFragment.show(mainActivity.getSupportFragmentManager(),"mark_favourite_dialog");

                }
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
                        //Log.e(LOG_TAG,"inside failure "+t.toString());

                    }
                });
            }
        });

    }

    private WebHookRegister createWebHookPayload(List<Integer> mSelectedItems) {
        WebHookRegister webHookRegister = new WebHookRegister();
        webHookRegister.setActive(true);
        webHookRegister.setName("web");
        Config config = new Config();
        config.setContent_type("json");
        config.setUrl(Constants.NOTIF_ROOT_URL+Constants.WEBHOOK_URL);
        webHookRegister.setConfig(config);
        List<String> eventList = new ArrayList<>();
        String[] events = mContext.getResources().getStringArray(R.array.favourite_events);
        for(Integer i:mSelectedItems){
            eventList.add(events[i]);
        }
        //eventList.add("push");
        webHookRegister.setEvents(eventList);

        return webHookRegister;
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
        public final SwitchCompat favouriteButton;
        public final View mView;


        public RepoAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.title);
            urlView = (TextView) itemView.findViewById(R.id.url);
            forkView = (TextView) itemView.findViewById(R.id.forks);
            languageView = (TextView) itemView.findViewById(R.id.language);
            favouriteButton = (SwitchCompat) itemView.findViewById(R.id.fav_button);
        }

    }
}
