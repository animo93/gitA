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
        public static final String COLUMN_TITLE = "repo_title";
        public static final String COLUMN_URL = "repo_url";
        public static final String COLUMN_FORK = "repo_forked";
        public static final String COLUMN_REPO_OWNER = "repo_owner";
        public static final String COLUMN_UPDATED_DATE = "updated_date";
        public static final String COLUMN_HOOK_ID="hook_id";
        public static final String COLUMN_REPO_DESC= "repo_desc";
        public static final String COLUMN_REPO_LANGUAGE="repo_language";
        public static final String COLUMN_REPO_FORK_COUNT = "repo_forks_count";
        public static final String COLUMN_REPO_ISSUE_COUNT = "repo_issue_count";
        public static final String COLUMN_REPO_STAR_COUNT = "repo_star_count";
        public static final String COLUMN_REPO_FAV = "repo_fav";

        public static final int COL_ID=0;
        public static final int COL_REPO_TITLE=1;
        public static final int COL_REPO_ID=2;
        public static final int COL_REPO_OWNER_ID=3;
        public static final int COL_UPDATED_DATE=4;
        public static final int COL_HOOK_ID=5;
        public static final int COL_REPO_DESC=6;
        public static final int COL_REPO_LANGUAGE=7;
        public static final int COL_REPO_FORK_COUNT=8;
        public static final int COL_REPO_ISSUE_COUNT=9;
        public static final int COL_REPO_STAR_COUNT=10;
        public static final int COL_REPO_FAV=11;
        public static final int COL_REPO_URL=12;
        public static final int COL_REPO_FORKED=13;


        public static final String[] REPO_COLUMNS = {
                TABLE_NAME+ "." + _ID,
                COLUMN_TITLE ,
                COLUMN_REPO_ID,
                COLUMN_REPO_OWNER,
                COLUMN_UPDATED_DATE,
                COLUMN_HOOK_ID,
                COLUMN_REPO_DESC,
                COLUMN_REPO_LANGUAGE,
                COLUMN_REPO_FORK_COUNT,
                COLUMN_REPO_ISSUE_COUNT,
                COLUMN_REPO_STAR_COUNT,
                COLUMN_REPO_FAV,
                COLUMN_URL,
                COLUMN_FORK

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
