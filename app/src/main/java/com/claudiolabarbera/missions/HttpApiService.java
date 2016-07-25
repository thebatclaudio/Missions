package com.claudiolabarbera.missions;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by labar on 20/07/2016.
 */
public class HttpApiService extends IntentService {

    private static final String TAG = "HttpApiService";

    public HttpApiService(){
        super("HttpApiService");
    }

    public HttpApiService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Start httpservice");

        /*Bundle extras = intent.getExtras();
        if(extras==null)return;*/

        HttpURLConnection connection = null;
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if(!sharedPref.getBoolean(getString(R.string.preferences_logged), false)){
                connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions", HttpUtils.METHOD_GET, null);
            } else {
                connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions", HttpUtils.METHOD_GET, null, sharedPref.getString(getString(R.string.preferences_token), ""));
            }
            Log.i(TAG, "STATUS "+connection.getResponseCode());
            String response = HttpUtils.readIt(connection.getInputStream());
            JsonParser.parseMissions(getBaseContext(), response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}