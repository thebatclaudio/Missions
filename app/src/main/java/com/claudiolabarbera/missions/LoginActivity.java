package com.claudiolabarbera.missions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity {

    protected final static String DEBUG_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLogged = sharedPref.getBoolean(getString(R.string.preferences_logged), false);

        if(isLogged) {
            finish();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText)findViewById(R.id.username);
                EditText password = (EditText)findViewById(R.id.password);

                String username_string = username.getText().toString();
                String password_string = password.getText().toString();
                new LoginTask(getBaseContext()).execute(new User(username_string, password_string));
            }
        });
    }

    private class LoginTask extends AsyncTask<User, Integer, String> {

        private Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

        protected String doInBackground(User... user) {
            HttpURLConnection connection = null;
            try {
                String params = "username="+user[0].getUsername()+"&password="+user[0].getPassword();
                connection = HttpUtils.buildConnection("http://dev.claudiolabarbera.com/login", HttpUtils.METHOD_POST, params);
                if(connection.getResponseCode() == 200) {
                    return HttpUtils.readIt(connection.getInputStream());
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(DEBUG_TAG, progress[0].toString());
        }

        protected void onPostExecute(String response) {
            try {
                if(response == null) {
                    Toast.makeText(getBaseContext(), "Bad credentials", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject user = JsonParser.parseUser(response);

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putBoolean(getString(R.string.preferences_logged), true);
                    editor.putString(getString(R.string.preferences_username), user.getString("username"));
                    editor.putString(getString(R.string.preferences_token), user.getString("token"));

                    editor.commit();

                    finish();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
