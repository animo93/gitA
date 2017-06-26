package com.example.animo.gita.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by animo on 8/6/17.
 */

public class RepoContract {

    public static final String CONTENT_AUTHORITY = "com.example.animo.gita";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REPOS = "repos";

    public static final class FavRepos implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPOS).build();

        public static final String TABLE_NAME = "repos";
        public static final String COLUMN_REPO_ID="repo_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ETAG = "etag";
        public static final String COLUMN_REPO_OWNER = "owner";
        public static final String COLUMN_UPDATED_DATE = "updated_date";

        public static final int COL_ID=0;
        public static final int COL_REPO_TITLE=1;
        public static final int COL_REPO_ID=2;
        public static final int COL_REPO_OWNER_ID=3;
        public static final int COL_ETAG_ID=4;


        public static final String[] REPO_COLUMNS = {
                RepoContract.FavRepos.TABLE_NAME+ "." + RepoContract.FavRepos._ID,
                RepoContract.FavRepos.COLUMN_TITLE ,
                RepoContract.FavRepos.COLUMN_REPO_ID,
                RepoContract.FavRepos.COLUMN_REPO_OWNER,
                RepoContract.FavRepos.COLUMN_ETAG
        };

        public static final String CONTENT_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_REPOS;

        public static final String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_REPOS;

        public static Uri buildRepoUriFromId(String repoId){
            return CONTENT_URI.buildUpon().appendPath(repoId).build();
        }

        public static Uri buildRepoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static String getRepoIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
