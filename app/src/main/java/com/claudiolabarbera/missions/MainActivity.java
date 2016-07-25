package com.claudiolabarbera.missions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";

    public final static String EXTRA_MISSION_ID = "com.claudiolabarbera.missions.MISSION_ID";
    public final static String EXTRA_MISSION_TITLE = "com.claudiolabarbera.missions.MISSION_TITLE";
    public final static String EXTRA_MISSION_PLACE = "com.claudiolabarbera.missions.MISSION_PLACE";
    public final static String EXTRA_MISSION_STATUS = "com.claudiolabarbera.missions.MISSION_STATUS";
    public final static String EXTRA_MISSION_LAT = "com.claudiolabarbera.missions.MISSION_LAT";
    public final static String EXTRA_MISSION_LNG = "com.claudiolabarbera.missions.MISSION_LNG";
    public final static String EXTRA_MISSION_USERNAME = "com.claudiolabarbera.missions.MISSION_USERNAME";

    private RecyclerView missionsRecyclerView;
    private RecyclerView.Adapter missionsAdapter;
    private RecyclerView.LayoutManager missionsLayoutManager;

    private ArrayList<Mission> missions;

    private boolean isReceiverRegistered = false;
    private UpdateBroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        missionsRecyclerView = (RecyclerView)findViewById(R.id.missions_recycler_view);
        missionsRecyclerView.setHasFixedSize(true);
        missionsLayoutManager = new LinearLayoutManager(this);
        missionsRecyclerView.setLayoutManager(missionsLayoutManager);

        Log.i(DEBUG_TAG, "START MAIN ACTIVITY");
    }

    protected void onResume() {
        super.onResume();
        Log.i(DEBUG_TAG, "onResume STARTED");

        Intent intent = new Intent(this, HttpApiService.class);
        startService(intent);

        Uri contentUri = Uri.withAppendedPath(Contract.BASE_CONTENT_URI, Contract.Mission.TABLE_NAME);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);

        Mission current = null;

        missions = new ArrayList<Mission>();

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Mission.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_TITLE));
            String place = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_PLACE));
            int status = cursor.getInt(cursor.getColumnIndex(Contract.Mission.COLUMN_STATUS));
            String lat = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_LAT));
            String lng = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_LNG));
            String username = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_USERNAME));

            current = new Mission(id, title, place, status, lat, lng, username);

            missions.add(current);
        }

        cursor.close();

        // specify an adapter (see also next example)
        missionsAdapter = new MissionsAdapter(missions, new MissionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Mission item) {
                Log.i(DEBUG_TAG, "Item clicked "+item.getTitle());
                Intent intent = new Intent(getBaseContext(), MissionDetailsActivity.class);
                intent.putExtra(EXTRA_MISSION_ID, item.getId());
                intent.putExtra(EXTRA_MISSION_TITLE, item.getTitle());
                intent.putExtra(EXTRA_MISSION_PLACE, item.getPlace());
                intent.putExtra(EXTRA_MISSION_STATUS, item.getStatus());
                intent.putExtra(EXTRA_MISSION_LAT, item.getLat());
                intent.putExtra(EXTRA_MISSION_LNG, item.getLng());
                intent.putExtra(EXTRA_MISSION_USERNAME, item.getUsername());

                startActivity(intent);
            }
        });
        missionsRecyclerView.setAdapter(missionsAdapter);

        if (!isReceiverRegistered) {
            if (receiver == null) {
                receiver = new UpdateBroadcastReceiver();
            }
            registerReceiver(receiver, new IntentFilter(JsonParser.INTENT_ACTION));
            isReceiverRegistered = true;
        }
    }

    protected void onPause() {
        super.onPause();
        if(isReceiverRegistered) {
            unregisterReceiver(receiver);
            receiver = null;
            isReceiverRegistered = false;
        }
    }

    private void updateData (Intent intent) {
        Uri contentUri = Uri.withAppendedPath(Contract.BASE_CONTENT_URI, Contract.Mission.TABLE_NAME);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);

        Mission current = null;

        missions = new ArrayList<Mission>();

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Mission.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_TITLE));
            String place = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_PLACE));
            int status = cursor.getInt(cursor.getColumnIndex(Contract.Mission.COLUMN_STATUS));
            String lat = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_LAT));
            String lng = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_LNG));
            String username = cursor.getString(cursor.getColumnIndex(Contract.Mission.COLUMN_USERNAME));

            current = new Mission(id, title, place, status, lat, lng, username);

            missions.add(current);
        }

        cursor.close();

        // specify an adapter (see also next example)
        missionsAdapter = new MissionsAdapter(missions, new MissionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Mission item) {
                Log.i(DEBUG_TAG, "Item clicked "+item.getTitle());
                Intent intent = new Intent(getBaseContext(), MissionDetailsActivity.class);
                intent.putExtra(EXTRA_MISSION_ID, item.getId());
                intent.putExtra(EXTRA_MISSION_TITLE, item.getTitle());
                intent.putExtra(EXTRA_MISSION_PLACE, item.getPlace());
                intent.putExtra(EXTRA_MISSION_STATUS, item.getStatus());
                intent.putExtra(EXTRA_MISSION_LAT, item.getLat());
                intent.putExtra(EXTRA_MISSION_LNG, item.getLng());
                intent.putExtra(EXTRA_MISSION_USERNAME, item.getUsername());

                startActivity(intent);
            }
        });
        missionsRecyclerView.setAdapter(missionsAdapter);
    }

    private class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateData(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPref.edit().clear().commit();
                finish();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
