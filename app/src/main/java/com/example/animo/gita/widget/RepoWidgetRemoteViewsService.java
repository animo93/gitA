package com.example.animo.gita.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.animo.gita.R;
import com.example.animo.gita.data.RepoContract;

import static com.example.animo.gita.data.RepoContract.FavRepos.REPO_COLUMNS;

/**
 * Created by animo on 22/6/17.
 */

public class RepoWidgetRemoteViewsService extends RemoteViewsService {
    private final static String LOG_TAG = RepoWidgetRemoteViewsService.class.getSimpleName();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Context context;
            private Cursor data = null;
            @Override
            public void onCreate() {
                Log.i(LOG_TAG,"inside onCreate");

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data=getContentResolver().query(
                        RepoContract.FavRepos.CONTENT_URI,
                        REPO_COLUMNS,
                        null,
                        null,
                        null);
                Log.d(LOG_TAG,"Data size "+data.getCount());
                Binder.restoreCallingIdentity(identityToken);


            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                Log.i(LOG_TAG,"inside getViewAt");
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item_repo);
                views.setTextViewText(R.id.title,data.getString(RepoContract.FavRepos.COL_REPO_TITLE));
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item_repo);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
