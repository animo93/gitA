package com.example.animo.gita.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.animo.gita.R;
import com.example.animo.gita.model.Files;

import java.util.List;

/**
 * Created by animo on 31/5/17.
 */

public class CommitFileAdapter extends ArrayAdapter<Files> {

    Context mContext;
    List<Files> files;
    public CommitFileAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Files> objects) {
        super(context, resource, objects);
        mContext=context;
        this.files= objects;
    }

    private static class ViewHolder {
        TextView titleView;
        TextView additionsView;
        TextView deltionsView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        Log.d(CommitFileAdapter.class.getSimpleName(),"title "+files.getFilename());
        viewHolder.titleView.setText(files.getFilename());
        viewHolder.additionsView.setText("+"+files.getAdditions());
        viewHolder.deltionsView.setText("-"+files.getDeletions());
        return result;


    }
}
