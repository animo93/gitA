package com.example.animo.gita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animo.gita.Constants;
import com.example.animo.gita.activity.FileViewActivity;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.R;
import com.example.animo.gita.activity.ReposDetailActivity;

import java.util.List;

/**
 * Created by animo on 24/5/17.
 */

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesAdapterViewHolder> {

    private static final String LOG_TAG = FilesAdapter.class.getSimpleName();


    private List<Files> filesList;

    private String repoName;
    private String owner;

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Files> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<Files> filesList) {
        this.filesList = filesList;
    }

    public FilesAdapter(Context context){
        this.mContext=context;
    }

    private Context mContext;
    @Override
    public FilesAdapter.FilesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.files,
                    parent,
                    false
            );
            view.setFocusable(true);
            return new FilesAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to Recycler view");
        }
    }

    @Override
    public void onBindViewHolder(FilesAdapter.FilesAdapterViewHolder holder, final int position) {
        holder.title.setText(filesList.get(position).getName());
        String type = filesList.get(position).getType();

       // Log.d(LOG_TAG,"File Name "+filesList.get(position).getName()+" and type "+type);
        if(type.equals("file")){
            Glide.with(mContext).load(R.drawable.ic_insert_drive_file_black_24dp)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100,100)
                    .into(holder.imageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),FileViewActivity.class);
                    intent.putExtra(Constants.URL,filesList.get(position).getDownload_url());
                    intent.putExtra(Constants.TITLE,filesList.get(position).getName());
                    mContext.startActivity(intent);
                }
            });

        } else {
            Glide.with(mContext).load(R.drawable.ic_folder_black_24dp)
                    .crossFade()
                    .override(100,100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d(LOG_TAG,"Path "+filesList.get(position).getPath());
                    Intent intent = new Intent(mContext,ReposDetailActivity.class);
                    intent.putExtra(Constants.REPO,repoName);
                    intent.putExtra(Constants.OWNER,owner);
                    intent.putExtra(Constants.PATH,filesList.get(position).getPath());
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return filesList==null ?0:filesList.size();
    }

    public class FilesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView imageView;
        public final View mView;

        public FilesAdapterViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.title = (TextView) itemView.findViewById(R.id.file_title);
            this.imageView = (ImageView) itemView.findViewById(R.id.fileImage);
        }
    }
}
