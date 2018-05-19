package com.example.toor.smashtimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CardView loginBtn;
    private TextView signup;
    private TextView loginFail;
    ProgressBar progressBar;

    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_temp);

        db = new DatabaseHelper(this);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginBtn = (CardView)findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.pBar);

        loginFail = findViewById(R.id.loginFail);
        signup = findViewById(R.id.signUp);
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebService.SIGNUPURL));
                startActivity(browserIntent);

            }
        });
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

                final String requesturl = WebService.getAuthReq(email, password);

                AsyncTask asyncTask = new AsyncTask()
                {
                    String status;
                    String role;
                    @Override
                    protected void onPreExecute()
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        emailEditText.setEnabled(false);
                        passwordEditText.setEnabled(false);
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
                        if(o == null)
                        {
                            loginFail.setText("Service Unavaliable please try again later");
                            acceptInput();
                            return;
                        }
                        try
                        {
                            JSONObject returnData = new JSONObject(o.toString());
                            status = returnData.getString("status");

                            if(!status.equalsIgnoreCase("pass"))
                            {
                                //password and id does not match
                                loginFail.setText("Username and password is incorrect");
                                acceptInput();
                                return;
                            }

                            role = returnData.getString("role");

                        } catch (JSONException e) {
                            loginFail.setText("Service Unavaliable please try again later");
                            acceptInput();
                            e.printStackTrace();
                            return;
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

        String prevUsername = preferences.getString("stusername", "");

        if(!prevUsername.equals(mUsername))
        {
            db.resetDatabase(db.TBCHILD | db.TBTASKS);
        }
        editor.putString("stusername", mUsername);
        editor.putString("stpassword", mPassword);
        editor.apply();
        Intent intent = new Intent (getBaseContext(), ChildList_activity.class);
        intent.putExtra("username", mUsername);
        startActivity(intent);
    }

    private void acceptInput()
    {
        progressBar.setVisibility(View.GONE);
        loginBtn.setEnabled(true);
        loginFail.setVisibility(View.VISIBLE);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
    }

}
