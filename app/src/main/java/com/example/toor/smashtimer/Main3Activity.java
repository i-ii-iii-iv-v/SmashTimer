package com.example.toor.smashtimer;

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

import java.io.IOException;
import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {


    String[] listItems = {"eat", "poop", "drink", "sleep", "get dressed", "brush teeth", "sleep", "study"};
    ArrayList<Task>[] tasklist;
    ArrayAdapter[] taskAdapter;
    ListView lv;
    DatabaseHelper db;
    CheckBox checkBoxes[];
    FloatingActionButton fab;
    int tabIndex;
    TabLayout tabLayout;
    SwipeMenuListView lvc;
    SwipeMenuCreator creator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        final String childId = intent.getStringExtra("childid");

        //tasklist contains list of tasks for each days in a week

        setViews();
//////////////////////////////////////////////////
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
        //////////////////////////////////////////////////
        tabIndex = 0; //set to monday; 'M'

        db = new DatabaseHelper(getApplicationContext());
        tasklist = db.queryInitTaskList(childId);


        taskAdapter = new ArrayAdapter[7];

        //adapter for each tabs of days in a week
        for(int i = 0; i < taskAdapter.length; i++)
        {
            taskAdapter[i] = new ArrayAdapter(this, R.layout.listview_main, tasklist[i]);
        }

        lvc.setAdapter(taskAdapter[tabIndex]);



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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main3Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
                //mBuilder.setTitle("Add Tssk");

                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                checkBoxes[0] = mView.findViewById(R.id.mBox);
                checkBoxes[1] = mView.findViewById(R.id.tuBox);
                checkBoxes[2] = mView.findViewById(R.id.wBox);
                checkBoxes[3] = mView.findViewById(R.id.thBox);
                checkBoxes[4] = mView.findViewById(R.id.fBox);
                checkBoxes[5] = mView.findViewById(R.id.saBox);
                checkBoxes[6] = mView.findViewById(R.id.suBox);
                final TimePicker startPicker = (TimePicker)mView.findViewById(R.id.simpleTimePicker);
                final TimePicker endPicker = (TimePicker)mView.findViewById(R.id.endTimePicker);
                ArrayAdapter<String> strAdapter = new ArrayAdapter<String>(Main3Activity.
                        this, android.R.layout.simple_spinner_item, listItems);
                strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(strAdapter);

                mBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        for(int j = 0; j < 7; j++)
                        {
                            if(checkBoxes[j].isChecked())
                            {
                                Task temp = new Task(mSpinner.getSelectedItem().toString(),childId,  startPicker.getCurrentHour(), startPicker.getCurrentMinute(), endPicker.getCurrentHour(),
                                        endPicker.getCurrentMinute(), j);
                                tasklist[j].add(temp);
                                db.addTask(childId, temp);

                            }
                        }

                        //if(mSpinner.getSelectedItem().toString().equalsIgnoreCase("")){
                        //
                        //}
                        //https://notifood.000webhostapp.com/addtask.php?username=aa&password=aa&task=eat%20lunch&startTime=6:30:00&endTime=7:00:00&childid=bb
                        String requesturl = "https://notifood.000webhostapp.com/addtask.php?username=aa&password=aa&task=";
                        requesturl+= mSpinner.getSelectedItem().toString().replace(" ", "%20");
                        requesturl+= "&startTime=" + startPicker.getCurrentHour().toString() +":"+startPicker.getCurrentMinute().toString()+":00&endTime=";
                        requesturl+= endPicker.getCurrentHour().toString() +":"+endPicker.getCurrentMinute().toString()+":00&childid="+childId;
                        final String url = requesturl;

                        //Log.e("3", url);

                        /*AsyncTask asyncTask = new AsyncTask()
                        {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(url).build();

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
                                Log.e("3", o.toString());

                            }

                        }.execute();*/

                        taskAdapter[tabIndex].notifyDataSetChanged();

                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

    }

    private void setViews()
    {
        lvc =  findViewById(R.id.taskListViewCustom);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        checkBoxes = new CheckBox[7];


    }


}

