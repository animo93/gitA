package com.example.animo.gita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.animo.gita.Constants;
import com.example.animo.gita.R;
import com.example.animo.gita.activity.FileDiffActivity;
import com.example.animo.gita.activity.ReposDetailActivity;
import com.example.animo.gita.model.Files;

import java.util.List;

/**
 * Created by animo on 31/5/17.
 */

public class CommitFileAdapter extends ArrayAdapter<Files> implements View.OnClickListener{
    public static final String LOG_TAG = CommitFileAdapter.class.getSimpleName();


    Context mContext;
    List<Files> files;
    public CommitFileAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Files> objects) {
        super(context, resource, objects);
        mContext=context;
        this.files= objects;
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG_TAG,"inside onClick");
        int position = (int) view.getTag();
        Files files= getItem(position);
        Intent intent = new Intent(mContext, FileDiffActivity.class);
        intent.putExtra(Constants.DIFF_CONTENT,files.getPatch());
        mContext.startActivity(intent);

    }

    private static class ViewHolder {
        TextView titleView;
        TextView additionsView;
        TextView deltionsView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Files files = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        final View result;

        if(convertView ==null){
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_commits,parent,false);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.additionsView = (TextView) convertView.findViewById(R.id.additions);
            viewHolder.deltionsView = (TextView) convertView.findViewById(R.id.deletions);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,"inside onClick");
                Files files= getItem(position);
                Intent intent = new Intent(view.getContext(), FileDiffActivity.class);
                intent.putExtra(Constants.DIFF_CONTENT,files.getPatch());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        Log.d(CommitFileAdapter.class.getSimpleName(),"title "+files.getFilename());
        viewHolder.titleView.setText(files.getFilename());
        viewHolder.additionsView.setText("+"+files.getAdditions());
        viewHolder.deltionsView.setText("-"+files.getDeletions());
        return result;


    }
}
