package com.example.animo.gita.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by animo on 8/6/17.
 */

public class RepoDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "repos.db";
    public RepoDbHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REPOS_TABLE= "CREATE TABLE "+ RepoContract.FavRepos.TABLE_NAME+ " ("+
                RepoContract.FavRepos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                RepoContract.FavRepos.COLUMN_REPO_ID + " TEXT NOT NULL, "+
                RepoContract.FavRepos.COLUMN_TITLE + " TEXT NOT NULL, " +
                RepoContract.FavRepos.COLUMN_ETAG + " TEXT, " +
                RepoContract.FavRepos.COLUMN_REPO_OWNER + " TEXT NOT NULL, " +
                RepoContract.FavRepos.COLUMN_UPDATED_DATE + " TEXT, " +
                RepoContract.FavRepos.COLUMN_HOOK_ID + " TEXT " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_REPOS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ RepoContract.FavRepos.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
