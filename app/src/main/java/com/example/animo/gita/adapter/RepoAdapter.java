package com.example.animo.gita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.activity.ReposDetailActivity;
import com.example.animo.gita.model.Repository;

import java.util.List;

/**
 * Created by animo on 8/5/17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewHolder> {

    private static final String LOG_TAG = RepoAdapter.class.getSimpleName();

    private List<Repository> repositoryList;

    private Context mContext;

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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ReposDetailActivity.class);
                Repository repo = getRepositoryList().get(position);
                intent.putExtra(Constants.REPO,repo.getName());
                intent.putExtra(Constants.OWNER,repo.getOwner().getLogin());
                intent.putExtra(Constants.TITLE,repo.getFullName());
                intent.putExtra(Constants.SOURCE, repo.getSource() == null ?
                        null : repo.getSource().getOwner().getLogin());
                intent.putExtra(Constants.DESC, repo.getDescription() == null ?
                        null : (String) repo.getDescription());
                intent.putExtra(Constants.LANG, repo.getLanguage() == null ?
                        null : repo.getLanguage());
                intent.putExtra(Constants.ISSUE_COUNT, repo.getOpenIssuesCount());
                intent.putExtra(Constants.STARGAZERS, repo.getStargazersCount());
                intent.putExtra(Constants.FORKS, repo.getForksCount());
                intent.putExtra(Constants.PATH,"");
                mContext.startActivity(intent);
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
        public final View mView;


        public RepoAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.title);
            urlView = (TextView) itemView.findViewById(R.id.url);
            forkView = (TextView) itemView.findViewById(R.id.forks);
            languageView = (TextView) itemView.findViewById(R.id.language);
        }

    }
}
