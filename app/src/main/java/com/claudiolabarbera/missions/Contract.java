package com.claudiolabarbera.missions;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by labar on 21/07/2016.
 */
public class Contract {

    public static final String CONTENT_AUTHORITY = "com.claudiolabarbera.missions";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Mission {

        public static final String TABLE_NAME = "missions";

        public static final String COLUMN_ID = TABLE_NAME + "_id";
        public static final String COLUMN_TITLE = TABLE_NAME + "_title";
        public static final String COLUMN_PLACE = TABLE_NAME + "_place";
        public static final String COLUMN_STATUS = TABLE_NAME + "_status";
        public static final String COLUMN_LAT = TABLE_NAME + "_lat";
        public static final String COLUMN_LNG = TABLE_NAME + "_lng";
        public static final String COLUMN_USERNAME = TABLE_NAME + "_username";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }
}