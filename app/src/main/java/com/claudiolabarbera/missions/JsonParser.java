package com.claudiolabarbera.missions;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by labar on 21/07/2016.
 */
public class JsonParser {

    private static final String DEBUG_TAG = "JsonParser";

    public static final String INTENT_ACTION = "com.claudiolabarbera.missions.receiver.intent.action.UPDATE";
    //private static final String INTENT_EXTRA = "extra";

    /**
     * Questo è il posto in cui salvare nel content provider!
     */
    public static final void parseMissions(Context context, String response) throws JSONException {

        JSONArray jMessages = new JSONArray(response);

        final int length = jMessages.length();

        Uri contentUri = Uri.withAppendedPath(com.claudiolabarbera.missions.Contract.BASE_CONTENT_URI, com.claudiolabarbera.missions.Contract.Mission.TABLE_NAME);

        //per prima cosa elimino tutti i dati
        context.getContentResolver().delete(contentUri, "1 = 1", null);

        for(int i=0;i<length;i++){

            JSONObject jMessage = jMessages.getJSONObject(i);

            final int id = jMessage.getInt(JSONKeys.KEY_ID);
            final String title = jMessage.getString(JSONKeys.KEY_TITLE);
            final String place = jMessage.getString(JSONKeys.KEY_PLACE);
            final int status = jMessage.getInt(JSONKeys.KEY_STATUS);
            final String lat = jMessage.getString(JSONKeys.KEY_LAT);
            final String lng = jMessage.getString(JSONKeys.KEY_LNG);
            final String username = jMessage.getString(JSONKeys.KEY_USERNAME);

            ContentValues values = new ContentValues();
            values.put(Contract.Mission.COLUMN_ID, id);
            values.put(Contract.Mission.COLUMN_TITLE, title);
            values.put(Contract.Mission.COLUMN_PLACE, place);
            values.put(Contract.Mission.COLUMN_STATUS, status);
            values.put(Contract.Mission.COLUMN_LAT, lat);
            values.put(Contract.Mission.COLUMN_LNG, lng);
            values.put(Contract.Mission.COLUMN_USERNAME, username);

            //e poi li inserisco ad uno ad uno
            context.getContentResolver().insert(contentUri, values);
        }

        //appena finisco invio la notifica broadcast
        Intent intent = new Intent();
        intent.setAction(INTENT_ACTION);
        //intent.putExtra(INTENT_EXTRA, "test");
        context.sendBroadcast(intent);
    }

    public static final JSONObject parseUser(String response) throws JSONException {
        return new JSONObject(response);
    }

    /**
     * Questa classe contiene le chiavi dei valori nel json che scaricate
     */
    private static final class JSONKeys{
        private static final String KEY_ID = "id";
        private static final String KEY_TITLE = "title";
        private static final String KEY_PLACE = "place";
        private static final String KEY_STATUS = "status";
        private static final String KEY_USERNAME = "username";
        private static final String KEY_LAT = "lat";
        private static final String KEY_LNG = "lng";
    }

}