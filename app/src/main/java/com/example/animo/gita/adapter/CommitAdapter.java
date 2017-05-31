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
import com.example.animo.gita.activity.CommitsDetailActivity;
import com.example.animo.gita.model.RepoCommit;

import java.util.List;

/**
 * Created by animo on 23/5/17.
 */

public class CommitAdapter extends RecyclerView.Adapter<CommitAdapter.CommitAdapterViewHolder> {

    private static final String LOG_TAG = CommitAdapter.class.getSimpleName();

    private List<RepoCommit> repoCommitList;
    private String repo;
    private String owner;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private Context mContext;

    public List<RepoCommit> getRepoCommitList() {
        return repoCommitList;
    }

    public void setRepoCommitList(List<RepoCommit> repoCommitList) {
        this.repoCommitList = repoCommitList;
    }

    public CommitAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public CommitAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.commit,
                    parent,
                    false
            );
            view.setFocusable(true);
            return new CommitAdapter.CommitAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(CommitAdapterViewHolder holder, final int position) {

        holder.titleView.setText(repoCommitList.get(position).getCommit().getMessage());
        holder.descView.setText(repoCommitList.get(position).getCommit().getCommitter().getDate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CommitsDetailActivity.class);
                RepoCommit repoCommit = getRepoCommitList().get(position);
                intent.putExtra(Constants.REPO,getRepo());
                intent.putExtra(Constants.OWNER,getOwner());
                intent.putExtra(Constants.SHA,repoCommit.getSha());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return repoCommitList==null ? 0:repoCommitList.size();
    }

    public class CommitAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView descView;
        public final View mView;

        public CommitAdapterViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.titleView = (TextView) itemView.findViewById(R.id.title);
            this.descView = (TextView) itemView.findViewById(R.id.desc);
        }
    }
}
