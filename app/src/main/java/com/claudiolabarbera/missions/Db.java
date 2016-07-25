package com.claudiolabarbera.missions;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by labar on 21/07/2016.
 */
public class Db extends SQLiteOpenHelper {

    private static final String DB_NAME = "vigilantes.db";
    private static final int DB_VERSION = 1;

    public Db(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Statements.MISSION_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO: Implement this method
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db){
        // TODO: Implement this method
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

}