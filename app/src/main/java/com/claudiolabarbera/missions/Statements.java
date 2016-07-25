package com.claudiolabarbera.missions;

/**
 * Created by labar on 21/07/2016.
 */
public class Statements {

    private static final String START_STATEMENTS = "create table if not exists ";

    public static final String MISSION_STATEMENT = START_STATEMENTS + Contract.Mission.TABLE_NAME + " (" +
            Contract.Mission.COLUMN_ID + " integer primary key, " +
            Contract.Mission.COLUMN_TITLE + " text, " +
            Contract.Mission.COLUMN_PLACE + " text, "+
            Contract.Mission.COLUMN_STATUS + " integer, "+
            Contract.Mission.COLUMN_USERNAME + " text, "+
            Contract.Mission.COLUMN_LAT + " text, "+
            Contract.Mission.COLUMN_LNG + " text) ;";
}