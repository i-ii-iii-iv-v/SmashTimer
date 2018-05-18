package com.example.toor.smashtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TaskList_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter[] adapter;
    private ArrayList<Task>[] listItems;

    private String childId;
    private Context context;
    private int tabIndex;
    private DatabaseHelper db;

    private TabLayout tabLayout;
    private FloatingActionButton fab;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_test);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mUsername = preferences.getString("stusername", "");

        context = this;
        Intent intent = getIntent();
        childId = intent.getStringExtra("childid");
        tabIndex = 0; //set to monday; 'M'

        setViews();

        db = new DatabaseHelper(getApplicationContext());
        listItems = db.queryInitTaskList(childId);

        //adapter for each tabs of days in a week
        adapter = new RecyclerView.Adapter[7];
        for(int i = 0; i < adapter.length; i++)
        {
            adapter[i] = new TaskItemAdapter(listItems[i], this);
        }

        recyclerView.setAdapter(adapter[tabIndex]);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final String deleteTaskurl = "http://comp4900group23.000webhostapp.com/removeTasks.php?email=" +childId
                        +"&groupid="+ mUsername+ "&taskName="+Utility.urlEncode(listItems[tabIndex].get(viewHolder.getAdapterPosition()).toString())
                        +"&start="+listItems[tabIndex].get(viewHolder.getAdapterPosition()).getStartTS() + "&end="
                        + listItems[tabIndex].get(viewHolder.getAdapterPosition()).getEndTS()+ "&day="
                        +listItems[tabIndex].get(viewHolder.getAdapterPosition()).getDay();
                Log.e("tasklistactivity: ", deleteTaskurl);

                db.removeTask(listItems[tabIndex].get(viewHolder.getAdapterPosition()));
                listItems[tabIndex].remove(viewHolder.getAdapterPosition());
                recyclerView.removeViewAt(viewHolder.getAdapterPosition());
                adapter[tabIndex].notifyItemRemoved(viewHolder.getAdapterPosition());


                AsyncTask getTask = new AsyncTask()
                {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(deleteTaskurl).build();

                        Response response = null;

                        try{
                            response = client.newCall(request).execute();
                            return response.body().string();

                        }catch(IOException e){
                            e.printStackTrace();
                            //save it into query table
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
                                //save in query table
                            }
                            JSONObject returnData = new JSONObject(o.toString());
                            status = returnData.getString("status");
                            if(status.equalsIgnoreCase("pass"))
                            {
                                return;
                                //handle errors:
                            }

                            else
                            {
                                return;//??? what are the cases?
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }.execute();
                //adapter[tabIndex].notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.e(Integer.toString(viewHolder.getAdapterPosition()), Integer.toString(listItems[tabIndex].size()));
                listItems[tabIndex].remove(viewHolder.getAdapterPosition());
                //adapter[tabIndex].notifyDataSetChanged();

            }
        }).attachToRecyclerView(recyclerView);*/
    }

    @Override
    public void onResume(){
        super.onResume();
        sync();
        // put your code here...

    }

    private void setViews()
    {
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        fab = (FloatingActionButton)findViewById(R.id.addtask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getBaseContext(), Add_Task_Activity.class);
                intent.putExtra("childId", childId);
                intent.putExtra("username", mUsername);
                startActivity(intent);

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                recyclerView.setAdapter(adapter[tabIndex]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void sync()
    {
        final String getTaskurl = "https://comp4900group23.000webhostapp.com/tasks.php?email=" + childId;

        AsyncTask getTask = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getTaskurl).build();

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
                        db.deleteAllTasksChild(childId);
                    }
                    db.deleteAllTasksChild(childId);
                    int numRows = rows.length();
                    //childlist = new JSONObject[numRows];
                    for(int i = 0; i < numRows; i++)
                    {
                        JSONObject row = rows.getJSONObject(i);
                        Task task = new Task(row.getString("description"),
                                row.getString("email"),
                                Utility.TStoHour(row.getString("start")),
                                Utility.TStoMin(row.getString("start")),
                                Utility.TStoHour(row.getString("end")),
                                Utility.TStoMin(row.getString("end")),
                                Integer.parseInt(row.getString("day")),
                                Integer.parseInt(row.getString("alarm")));

                        db.addTask(childId, task);

                    }
                    //parseJson first


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listItems = db.queryInitTaskList(childId);
                adapter = new RecyclerView.Adapter[7];
                for(int i = 0; i < adapter.length; i++)
                {
                    adapter[i] = new TaskItemAdapter(listItems[i], context);
                }

                recyclerView.setAdapter(adapter[tabIndex]);
            }

        }.execute();

    }
}
