package com.claudiolabarbera.missions;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by labar on 21/07/2016.
 */
public class Provider extends ContentProvider {
    private Db db;

    private static final String TAG = "provider";

    private static final int CODE_MISSIONS = 100;
    private static final int CODE_MISSION = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.Mission.TABLE_NAME,
                CODE_MISSIONS);

        matcher.addURI(authority, Contract.Mission.TABLE_NAME + "/#",
                CODE_MISSION);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        //istanzio il db qui
        db = new Db(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = null;

        switch (sUriMatcher.match(uri)) {

            case CODE_MISSIONS:
                type = Contract.Mission.CONTENT_TYPE;
                break;

            case CODE_MISSION:
                type = Contract.Mission.CONTENT_ITEM_TYPE;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return type;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {

            case CODE_MISSIONS:

                cursor = db.getReadableDatabase().query(
                        Contract.Mission.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder
                );

                break;

            case CODE_MISSION:

                cursor = db.getReadableDatabase().query(
                        Contract.Mission.TABLE_NAME,
                        projection,
                        Contract.Mission.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()},
                        null, null, sortOrder
                );

                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri = null;
        long id = -1;

        switch (sUriMatcher.match(uri)) {

            case CODE_MISSIONS:

                id = db.getWritableDatabase().insert(
                        Contract.Mission.TABLE_NAME,
                        null, values
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        returnUri = ContentUris.withAppendedId(uri, id);

        Log.d(TAG, "insert: " + uri.toString() + ", " + returnUri.toString());

        if(!returnUri.getLastPathSegment().equals("-1"))
            getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs){

        int rows = 0;

        final SQLiteDatabase myDb = db.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case CODE_MISSIONS:
                rows = myDb.delete(Contract.Mission.TABLE_NAME, where, whereArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if(rows>0)
            getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "delete: " + uri.toString() + ", " + rows);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs){
        // TODO: Implement this method

        int rows = 0;

        switch (sUriMatcher.match(uri)){

            case CODE_MISSION:

                rows = db.getWritableDatabase().update(
                        Contract.Mission.TABLE_NAME,
                        values,
                        Contract.Mission.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()}
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if(rows>0)
            getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "update: " + uri.toString() + ", " + rows);

        return rows;
    }
}
