package com.example.animo.gita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.activity.FileDiffActivity;
import com.example.animo.gita.model.Commit;
import com.example.animo.gita.model.Files;

import java.util.List;
import java.util.Map;

/**
 * Created by animo on 27/6/17.
 */

public class CommitDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Map<String,List<Files>> mapFileList;

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    private Commit commit;
    public static final String LOG_TAG = CommitDetailAdapter.class.getSimpleName();


    public CommitDetailAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG,"inside onCreateViewHolder");
        if(parent instanceof RecyclerView){
            {
                if(viewType==0){
                    View view = LayoutInflater.from(mContext).inflate(
                            R.layout.commit_desc,
                            parent,
                            false
                    );
                    view.setFocusable(true);
                    return new CommitDetailAdapter.CommitDescViewHolder(view);
                }else {
                    View view = LayoutInflater.from(mContext).inflate(
                            R.layout.commit_detail,
                            parent,
                            false
                    );
                    view.setFocusable(true);
                   /* int height = parent.getMeasuredHeight();
                    int width = parent.getMeasuredWidth();
                    view.setLayoutParams(new RecyclerView.LayoutParams(width,height));*/
                    return new CommitDetailAdapter.CommitDetailViewHolder(view);
                }

            }
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == 0){
            CommitDescViewHolder holder0 = (CommitDescViewHolder) holder;
            //holder0.commiterView.setText(commit.getCommitter().getLogin());
            holder0.commitName.setText(commit.getCommit().getMessage());
            holder0.commitDesc.setText("Showing "+commit.getFiles().size()+" changed files with "+
                    commit.getStats().getAdditions()+" additions and "+commit.getStats().getDeletions()+" deletions");

        }else{
            CommitDetailViewHolder holder1 = (CommitDetailViewHolder) holder;
            String key = (String) mapFileList.keySet().toArray()[position-1];
            holder1.typeView.setText("Files "+ key);
            final List<Files> value = mapFileList.get(key);
            Log.d(LOG_TAG,"Size of "+key+" "+value.size());

            for(int i=0;i<value.size();i++){
                View view = LayoutInflater.from(mContext).inflate(R.layout.file_commits,holder1.layout,false);
                TextView title = (TextView) view.findViewById(R.id.file_name);
                TextView additionsView = (TextView) view.findViewById(R.id.additions);
                TextView deltionsView = (TextView) view.findViewById(R.id.deletions);

                title.setText(value.get(i).getFilename());
                additionsView.setText("+"+value.get(i).getAdditions());
                deltionsView.setText("-"+value.get(i).getDeletions());

                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        {
                            Log.i(LOG_TAG,"inside onClick");
                            Files files= value.get(finalI);
                            Intent intent = new Intent(mContext, FileDiffActivity.class);
                            intent.putExtra(Constants.TITLE,files.getName());
                            intent.putExtra(Constants.DIFF_CONTENT,files.getPatch());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);

                        }
                    }
                });

                holder1.layout.addView(view);
            }
            /*CommitFileAdapter adapter = new CommitFileAdapter(mContext,holder1.listView.getId(),value);
            holder1.listView.setAdapter(adapter);

            ListAdapter adapter1 = holder1.listView.getAdapter();
            int noOfItems = adapter1.getCount();
            Log.d(LOG_TAG,"Count "+noOfItems);

            int totalItemsHeight = 0;
            for(int i=0;i<noOfItems;i++){
                View item = adapter1.getView(i,null,holder1.listView);
                item.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED);
                totalItemsHeight += item.getMeasuredHeight() + item.getPaddingBottom() +item.getPaddingTop();
            }
            int totalDividersHeight = holder1.listView.getDividerHeight() * (noOfItems);
            Log.d(LOG_TAG,"Total height "+totalItemsHeight);

            holder1.typeView.measure(0,0);
            int textHeight = holder1.typeView.getMeasuredHeight();
            Log.d(LOG_TAG,"Text heigh "+textHeight);

            ViewGroup.LayoutParams params = holder1.listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + textHeight;
            holder1.listView.setLayoutParams(params);
            holder1.listView.requestLayout();*/
        }

    }


    @Override
    public int getItemCount() {
        return mapFileList==null ? 0:mapFileList.size()+1;

    }

    public Map<String, List<Files>> getMapFileList() {
        return mapFileList;
    }

    public void setMapFileList(Map<String, List<Files>> mapFileList) {
        this.mapFileList = mapFileList;
    }

    public class CommitDetailViewHolder extends RecyclerView.ViewHolder {

        public final TextView typeView;
        //public final ListView listView;
        public final LinearLayout layout;
        public CommitDetailViewHolder(View itemView) {
            super(itemView);
            this.typeView = (TextView) itemView.findViewById(R.id.commit_type);
            //this.listView = (ListView) itemView.findViewById(R.id.commit_files_list);
            this.layout = (LinearLayout) itemView.findViewById(R.id.commit_files_list);
        }
    }

    public class CommitDescViewHolder extends RecyclerView.ViewHolder{

        /*public TextView commiterView;
        public TextView daysView;*/
        public TextView commitName;
        public TextView commitDesc;

        public CommitDescViewHolder(View itemView) {
            super(itemView);
           /* this.commiterView = (TextView)itemView.findViewById(R.id.commiter_name);
            this.daysView = (TextView) itemView.findViewById(R.id.days);*/
            this.commitName = (TextView) itemView.findViewById(R.id.commit_name);
            this.commitDesc = (TextView) itemView.findViewById(R.id.commit_desc);
        }
    }
}
