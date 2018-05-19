package com.example.toor.smashtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class child_list_activity extends AppCompatActivity {

    private String mUsername;
    private String mPassword;

    private DatabaseHelper db;
    private Context context;

    private ArrayList<Child> cl;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    public void onBackPressed()
    {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_list);


        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mUsername = preferences.getString("stusername", "");
        mPassword = preferences.getString("stpassword", "");
        db = new DatabaseHelper(getApplicationContext());
        context = this;

        cl = db.initChildList(); //initialize list

        adapter = new ChildItemAdapter(cl, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        sync();
        // put your code here...

    }

    private void sync() {


        final String getChildurl = "http://comp4900group23.000webhostapp.com/users.php?groupId=" + mUsername;

        AsyncTask getChild = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getChildurl).build();

                Response response = null;

                try {
                    response = client.newCall(request).execute();
                    return response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.e("main: ", "kk");
                String status;
                try {
                    if (o == null) {
                        return;
                    }
                    JSONObject returnData = new JSONObject(o.toString());
                    status = returnData.getString("status");
                    if (!status.equalsIgnoreCase("pass")) {

                        return;
                        //handle errors:
                    }

                    JSONArray rows = returnData.getJSONArray("data");
                    if (rows == null) {
                        return;
                    }
                    db.resetDatabase(DatabaseHelper.TBCHILD);
                    int numRows = rows.length();
                    //childlist = new JSONObject[numRows];
                    for (int i = 0; i < numRows; i++) {
                        JSONObject row = rows.getJSONObject(i);
                        db.addChildTest(row.getString("email"), "nulll");


                    }
                    //parseJson first


                } catch (JSONException e) {
                    Log.e("childList: ", o.toString());
                    e.printStackTrace();
                }
                adapter = new ChildItemAdapter(cl, context);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }.execute();
    }

    private void startTaskActivity(int i)
    {
        Intent intent = new Intent(context, TaskList_Activity.class);
        Log.e("main", cl.get(i).getid());
        intent.putExtra("childid", cl.get(i).getid());
        startActivity(intent);
    }
}
