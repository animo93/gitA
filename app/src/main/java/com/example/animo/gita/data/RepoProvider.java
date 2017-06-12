package com.example.animo.gita.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by animo on 8/6/17.
 */

public class RepoProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int REPOS = 100;
    static final int REPOS_WITH_IDS = 102;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=RepoContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority,RepoContract.PATH_REPOS,REPOS);
        uriMatcher.addURI(authority,RepoContract.PATH_REPOS+"/#",REPOS_WITH_IDS);

        return uriMatcher;
    }

    private RepoDbHelper repoDbHelper;

    @Override
    public boolean onCreate() {
        repoDbHelper=new RepoDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case REPOS: {
                retCursor=repoDbHelper.getReadableDatabase().query(RepoContract.FavRepos.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case REPOS_WITH_IDS: {
                retCursor=getMoviesByMovieId(uri,projection,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri not matching "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    private Cursor getMoviesByMovieId(Uri uri, String[] projection, String sortOrder) {
        String selection;
        String[] selectionArgs;

        selection=RepoContract.FavRepos.TABLE_NAME+
                "." + RepoContract.FavRepos.COLUMN_REPO_ID + " = ? ";
        selectionArgs=new String[]{RepoContract.FavRepos.getRepoIdFromUri(uri)};

        return repoDbHelper.getReadableDatabase().query(
                RepoContract.FavRepos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match=sUriMatcher.match(uri);

        switch (match){
            case REPOS:{
                return RepoContract.FavRepos.CONTENT_TYPE;
            }
            case REPOS_WITH_IDS: {
                return RepoContract.FavRepos.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Uri Not Found "+uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db=repoDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case REPOS: {
                long _id=db.insert(RepoContract.FavRepos.TABLE_NAME,null,contentValues);
                if( _id > 0 ){
                    returnUri=RepoContract.FavRepos.buildRepoUri(_id);
                }
                else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Uri not found "+uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=repoDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsDeleted;

        if(null==selection)
            selection="1";
        switch (match){
            case REPOS: {
                rowsDeleted=db.delete(RepoContract.FavRepos.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Uri not found "+uri);
            }
        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db=repoDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case REPOS: {
                rowsUpdated=db.update(RepoContract.FavRepos.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Uri not found"+uri);
            }
        }
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }
}
