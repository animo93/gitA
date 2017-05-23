package com.example.animo.gita;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                intent.putExtra(Constants.REPO,repositoryList.get(position).getName());
                intent.putExtra(Constants.OWNER,repositoryList.get(position).getOwner().getLogin());
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
