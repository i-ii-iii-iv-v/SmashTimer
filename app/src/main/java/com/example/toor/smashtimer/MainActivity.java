package com.example.toor.smashtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;

public class MainActivity extends AppCompatActivity {

    /*
    layout to create list ui
     */
    ListView lv;

    /*
    Contains list of child added by the user
     */
    ArrayList<Child> cl;

    /*
    makes list based on cl arraylist
     */
    String mUsername;
    String mPassword;
    ArrayAdapter adapter;
    DatabaseHelper db;
    Context context;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mUsername = preferences.getString("stusername", "");
        mPassword = preferences.getString("stpassword", "");
        db = new DatabaseHelper(getApplicationContext());
        context = this;
        lv = (ListView) findViewById(R.id.listView);
        cl = db.initChildList(); //initialize list
        adapter = new ArrayAdapter(context, R.layout.listview_main, cl);
        lv.setAdapter(adapter);
        //--> test case*****************************************************************************************

        //-->***************************************************************************
        //db.testQueries();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startTaskActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        sync();
        // put your code here...

    }
    /*
    go to new activity based on item selected from the list. param i is the index of item selected
     */
    private void startTaskActivity(int i)
    {
        Intent intent = new Intent(this, Main3Activity.class);
        Log.e("main", cl.get(i).getid());
        intent.putExtra("childid", cl.get(i).getid());
        startActivity(intent);
    }

    private void sync()
    {

        //final String timestampurl = "http://comp4900group23.000webhostapp.com/getTimestamp.php?email="+mUsername;
        final String getChildurl = "http://comp4900group23.000webhostapp.com/users.php?groupId=" + mUsername;

        AsyncTask getChild = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getChildurl).build();

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
                Log.e("main: ", "kk");
                String status;
                try
                {
                    if(o == null)
                    {
                        return;
                    }
                    JSONObject returnData = new JSONObject(o.toString());
                    status = returnData.getString("status");
                    if(!status.equalsIgnoreCase("pass"))
                    {
                        return;
                        //handle errors:
                    }

                    JSONArray rows = returnData.getJSONArray("data");
                    if(rows==null)
                    {
                        return;
                    }
                    db.resetDatabase(DatabaseHelper.TBCHILD);
                    int numRows = rows.length();
                    //childlist = new JSONObject[numRows];
                    for(int i = 0; i < numRows; i++)
                    {
                        JSONObject row = rows.getJSONObject(i);
                        db.addChildTest(row.getString("email"), "nulll");


                    }
                    //parseJson first


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("cl size:", Integer.toString(cl.size()));
                cl = db.initChildList(); //initialize list
                adapter = new ArrayAdapter(context, R.layout.listview_main, cl);
                lv.setAdapter(adapter);
            }

        }.execute();
/*
        AsyncTask timestampTask = new AsyncTask()
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
                String cTSServer;
                String cTSClient;
                String status;
                Date dateServer;
                try
                {
                    JSONObject returnData = new JSONObject(o.toString());
                    status = returnData.getString("status");
                    if(!status.equalsIgnoreCase("pass"))
                    {
                        return;
                        //possible errors:
                    }
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cTSClient = preferences.getString(mUsername+"ChildTS", "");
                    cTSServer= returnData.getString("timestamp");
                    if(Utility.clientTSbeforeServerTs(cTSClient, cTSServer))
                    {

                        db.resetDatabase(db.TBCHILD);
                    }
                    else
                    {
                        return;
                    }


                    //parseJson first


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();*/
    }


}
