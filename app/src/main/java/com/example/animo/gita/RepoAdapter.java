package com.example.animo.gita;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by animo on 8/5/17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewHolder> {
    @Override
    public RepoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(parent.getContext()).inflate(
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
    public void onBindViewHolder(RepoAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RepoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public RepoAdapterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
