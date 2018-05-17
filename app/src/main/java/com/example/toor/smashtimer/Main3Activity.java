package com.example.toor.smashtimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    ArrayList<Task>[] tasklist;
    ArrayAdapter[] taskAdapter;
    //ListView lv;
    DatabaseHelper db;
    CheckBox checkBoxes[];
    FloatingActionButton fab;
    int tabIndex;
    TabLayout tabLayout;
    SwipeMenuListView lvc;
    SwipeMenuCreator creator;
    String childId;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        context = this;
        Intent intent = getIntent();
        childId = intent.getStringExtra("childid");
        tabIndex = 0; //set to monday; 'M'

        setViews();


        db = new DatabaseHelper(getApplicationContext());
        tasklist = db.queryInitTaskList(childId);
        taskAdapter = new ArrayAdapter[7];

        //adapter for each tabs of days in a week
        for(int i = 0; i < taskAdapter.length; i++)
        {
            taskAdapter[i] = new ArrayAdapter(this, R.layout.listview_main, tasklist[i]);
        }

        lvc.setAdapter(taskAdapter[tabIndex]);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        sync();
        // put your code here...

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
                tasklist = db.queryInitTaskList(childId);
                taskAdapter = new ArrayAdapter[7];

                //adapter for each tabs of days in a week
                for(int i = 0; i < taskAdapter.length; i++)
                {
                    taskAdapter[i] = new ArrayAdapter(context, R.layout.listview_main, tasklist[i]);
                }

                lvc.setAdapter(taskAdapter[tabIndex]);
            }

        }.execute();

    }

    private void setViews()
    {
        lvc =  findViewById(R.id.taskListViewCustom);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        checkBoxes = new CheckBox[7];
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setIcon(R.drawable.ic_edit);
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a iconedit
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        lvc.setMenuCreator(creator);
        lvc.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        // open
                        break;
                    case 1:

                        if(db.removeTask((Task)taskAdapter[tabIndex].getItem(position)))
                        {
                            taskAdapter[tabIndex].remove(taskAdapter[tabIndex].getItem(position));
                            taskAdapter[tabIndex].notifyDataSetChanged();
                        }
                        else
                        {
                            Log.d("Bug: " ," deleting failed" );
                        }
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                lvc.setAdapter(taskAdapter[tabIndex]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


}

