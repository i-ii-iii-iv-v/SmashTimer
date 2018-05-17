package com.example.toor.smashtimer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CardView loginBtn;
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_PASSWORD = "key_password";
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    private String mChildTSfromServer;
    private Date mDateFromServer;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_temp);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginBtn = (CardView)findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.pBar);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setEnabled(false);
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Please Enter Email");
                    emailEditText.requestFocus();
                    loginBtn.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Please Enter Password");
                    passwordEditText.requestFocus();
                    loginBtn.setEnabled(true);
                    return;
                }

                final String requesturl ="http://comp4900group23.000webhostapp.com/authUsers.php?email="+emailEditText.getText().toString()+"&password="+passwordEditText.getText().toString();

                AsyncTask asyncTask = new AsyncTask()
                {
                    String status;
                    String role;
                    @Override
                    protected void onPreExecute()
                    {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(requesturl).build();

                        Response response = null;

                        try{
                            response = client.newCall(request).execute();
                            return response.body().string();

                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o)
                    {
                        try
                        {
                            JSONObject returnData = new JSONObject(o.toString());
                            status = returnData.getString("status");

                            if(!status.equalsIgnoreCase("pass"))
                            {
                                //password and id does not match
                                progressBar.setVisibility(View.GONE);
                                loginBtn.setEnabled(true);
                                return;
                            }

                            role = returnData.getString("role");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(role.equalsIgnoreCase("parent"))
                        {
                            loginParent();
                            //log in as parent
                        }
                        else if(role.equalsIgnoreCase("child"))
                        {
                            //log in as parent
                        }

                        /*AsyncTask timestampTask = new AsyncTask()
                        {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(timestampurl).build();

                                Response response = null;

                                try{
                                    response = client.newCall(request).execute();
                                    return response.body().string();

                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Object o)
                            {
                                try
                                {
                                    JSONObject obj = new JSONObject(o.toString());
                                    mChildTSfromServer = obj.getString("timestamp");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(!(mChildTSfromServer =="") && !(mChildTSfromServer == null))
                                {
                                    ParsePosition pos = new ParsePosition(0);
                                    mDateFromServer = dateFmt.parse(mChildTSfromServer, pos);
                                    //parseJson first
                                    loginUser();
                                }

                            }

                        }.execute();*/
                    }
                }.execute();




            }
        });
    }

    private void loginParent() {
        String mUsername = emailEditText.getText().toString();
        String mPassword = passwordEditText.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("stusername", mUsername);
        editor.putString("stpassword", mPassword);
        editor.apply();
        String storedChildTS = preferences.getString(mUsername+"ChildTS", "");
        if(storedChildTS == null || storedChildTS.equals(""))
        {
            storedChildTS = "2018-01-01 00:00:00";
            String taskTS = "2018-01-01 00:00:00";
            editor.putString(mUsername+"childClientTS", storedChildTS);
            editor.putString(mUsername + "TaskClientTS", taskTS);
            editor.apply();

            //initial pull from database;
        }
        Intent intent = new Intent (getBaseContext(), MainActivity.class);
        intent.putExtra("username", mUsername);
        startActivity(intent);
        /*String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Please Enter Email");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Please Enter Password");
            passwordEditText.requestFocus();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //editor.putString(KEY_EMAIL, email);
        //editor.putString(KEY_PASSWORD, password);
        //editor.commit();*/

        /*

        ParsePosition pos = new ParsePosition(0);
        Date d = dateFmt.parse(storedChildTS, pos);
        Log.e("login.java: ", d.toString());
        Log.e("login.java: ", mDateFromServer.toString());
        if(d.before(mDateFromServer))
        {
            //pull from server
            editor.putString(mUsername+"ChildTS", mChildTSfromServer);
            editor.apply();

            Log.e("login.java: ", d.toString());
        }*/
        //parseJson first
/*
        final String requesturl = "http://comp4900group23.000webhostapp.com/users.php?groupId="+ mUsername;
        try
        {
            JSONArray json = new JSONArray(o.toString());
            int n = json.length();
            for(int i = 0; i < n; i++)
            {
                JSONObject data = json.getJSONObject(i);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!(mChildTSfromServer =="") && !(mChildTSfromServer == null))
        {
            ParsePosition pos = new ParsePosition(0);
            mDateFromServer = dateFmt.parse(mChildTSfromServer, pos);
            //parseJson first
            loginUser();
        }
*/


    }




}
