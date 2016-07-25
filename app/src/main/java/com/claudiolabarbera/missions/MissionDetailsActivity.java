package com.claudiolabarbera.missions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MissionDetailsActivity extends AppCompatActivity {
    private final String DEBUG_TAG = "MissionDetails";

    protected int id = -1;
    private String title = null;
    private String place = null;
    protected int status = -1;
    private String lat = null;
    private String lng = null;
    private String url = null;
    private String username = null;

    protected Button button = null;
    protected Button restartButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_details);

        Log.i(DEBUG_TAG, "MissionDetails started");

        Intent intent = getIntent();
        this.id = intent.getIntExtra(MainActivity.EXTRA_MISSION_ID, -1);
        this.title = intent.getStringExtra(MainActivity.EXTRA_MISSION_TITLE);
        this.place = intent.getStringExtra(MainActivity.EXTRA_MISSION_PLACE);
        this.status = intent.getIntExtra(MainActivity.EXTRA_MISSION_STATUS, -1);
        this.lat = intent.getStringExtra(MainActivity.EXTRA_MISSION_LAT);
        this.lng =  intent.getStringExtra(MainActivity.EXTRA_MISSION_LNG);
        this.username =  intent.getStringExtra(MainActivity.EXTRA_MISSION_USERNAME);

        url = "http://maps.google.com/maps/api/staticmap?center=" + this.lat + "," + this.lng + "&zoom=19&size=500x500&sensor=false&markers=color:red%7C"+this.lat+","+this.lng;

        Log.i(DEBUG_TAG, url);

        TextView title = (TextView)findViewById(R.id.title);
        TextView place = (TextView)findViewById(R.id.place);
        TextView username = (TextView)findViewById(R.id.username);
        ImageView map = (ImageView)findViewById(R.id.map);
        this.button = (Button)findViewById(R.id.button);
        this.restartButton = (Button)findViewById(R.id.button_restart);

        Glide.with(this).load(url).into(map);

        title.setText(this.title);
        place.setText(this.place);

        Log.i(DEBUG_TAG, this.username);

        username.setText(this.username+" is doing this mission.");

        Log.i(DEBUG_TAG, "STATUS "+this.status);

        setOnClickListener(this.status);
    }

    protected void setOnClickListener(int status) {
        switch(this.status){
            case 0:
                button.setText("Start mission");
                button.setEnabled(true);
                restartButton.setEnabled(false);
                this.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(DEBUG_TAG, "CLICK");
                        new StartMissionTask().execute((Integer)id);
                    }
                });
                break;
            case 1:
                //se status è 1 allora la missione è già stata presa in carico, quindi cambio il testo del pulsante
                button.setText("Complete mission");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StopMissionTask().execute((Integer)id);
                    }
                });
                break;
            case 2:
                //se la missione è stata completata il pulsante è disabilitato
                button.setEnabled(false);
                button.setText("Mission completed");
                restartButton.setEnabled(true);
                restartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new RestartMissionTask().execute((Integer)id);
                    }
                });
                break;
        }
    }

    private class StartMissionTask extends AsyncTask<Integer, Integer, Integer> {
        protected Integer doInBackground(Integer... id) {
            Integer result = 0;
            HttpURLConnection connection = null;
            try {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(!sharedPref.getBoolean(getString(R.string.preferences_logged), false)){
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/start", HttpUtils.METHOD_POST, "");
                } else {
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/start", HttpUtils.METHOD_POST, "", sharedPref.getString(getString(R.string.preferences_token), ""));
                }
                return connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(DEBUG_TAG, progress[0].toString());
        }

        protected void onPostExecute(Integer result) {
            Log.i(DEBUG_TAG, result.toString());
            if(result != 200) {
                Toast.makeText(getBaseContext(), "Ops! There was an error", Toast.LENGTH_LONG).show();
            } else {
                status = 1;
                setOnClickListener(status);
            }
        }
    }

    private class StopMissionTask extends AsyncTask<Integer, Integer, Integer> {
        protected Integer doInBackground(Integer... id) {
            Integer result = 0;
            HttpURLConnection connection = null;
            try {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(!sharedPref.getBoolean(getString(R.string.preferences_logged), false)){
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/stop", HttpUtils.METHOD_POST, "");
                } else {
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/stop", HttpUtils.METHOD_POST, "", sharedPref.getString(getString(R.string.preferences_token), ""));
                }
                return connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(DEBUG_TAG, progress[0].toString());
        }

        protected void onPostExecute(Integer result) {
            Log.i(DEBUG_TAG, result.toString());
            if(result != 200) {
                Toast.makeText(getBaseContext(), "Ops! There was an error", Toast.LENGTH_LONG).show();
            } else {
                status = 2;
                setOnClickListener(status);
            }
        }
    }

    private class RestartMissionTask extends AsyncTask<Integer, Integer, Integer> {
        protected Integer doInBackground(Integer... id) {
            Integer result = 0;
            HttpURLConnection connection = null;
            try {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(!sharedPref.getBoolean(getString(R.string.preferences_logged), false)){
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/restart", HttpUtils.METHOD_POST, "");
                } else {
                    connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/missions/"+id[0].toString()+"/restart", HttpUtils.METHOD_POST, "", sharedPref.getString(getString(R.string.preferences_token), ""));
                }
                return connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(DEBUG_TAG, progress[0].toString());
        }

        protected void onPostExecute(Integer result) {
            Log.i(DEBUG_TAG, result.toString());
            if(result != 200) {
                Toast.makeText(getBaseContext(), "Ops! There was an error", Toast.LENGTH_LONG).show();
            } else {
                status = 0;
                setOnClickListener(status);
            }
        }
    }
}
