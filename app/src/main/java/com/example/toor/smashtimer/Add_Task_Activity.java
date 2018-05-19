package com.example.toor.smashtimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class Add_Task_Activity extends AppCompatActivity {

    FloatingActionButton fab;
    String[] listItems = {"Choose a task", "eat", "poop", "drink", "sleep", "get dressed", "brush teeth", "sleep", "study"};


    EditText customTaskNameEditText;
    Spinner taskListView;
    CheckBox taskNameCheckbox;
    CheckBox[] dayCheckboxes;
    TimePicker startPicker;
    TimePicker endPicker;
    Switch alarmSwitch;

    DatabaseHelper db;

    private String mUsername;
    private String childId;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtaskform);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = this;
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        childId = intent.getStringExtra("childId");
        mUsername = intent.getStringExtra("username");

        startPicker = findViewById(R.id.startTimePicker);
        endPicker = findViewById(R.id.endTimePicker);

        customTaskNameEditText = findViewById(R.id.customTaskName);
        customTaskNameEditText.setEnabled(false);
        customTaskNameEditText.setBackgroundColor(Color.LTGRAY);

        dayCheckboxes = new CheckBox[7];
        dayCheckboxes[0] = findViewById(R.id.mBox);
        dayCheckboxes[1] = findViewById(R.id.tuBox);
        dayCheckboxes[2] = findViewById(R.id.wBox);
        dayCheckboxes[3] = findViewById(R.id.thBox);
        dayCheckboxes[4] = findViewById(R.id.fBox);
        dayCheckboxes[5] = findViewById(R.id.saBox);
        dayCheckboxes[6] = findViewById(R.id.suBox);

        alarmSwitch = findViewById(R.id.alarmSwitch);

        taskListView = findViewById(R.id.taskName);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, listItems);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskListView.setAdapter(spinnerArrayAdapter);

        taskNameCheckbox = findViewById(R.id.taskNameCheckBox);
        taskNameCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskNameCheckbox.isChecked())
                {
                    customTaskNameEditText.setEnabled(true);
                    customTaskNameEditText.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    customTaskNameEditText.setEnabled(false);
                    customTaskNameEditText.setBackgroundColor(Color.LTGRAY);

                }

            }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites: //cancel

                                break;
                            case R.id.action_schedules: //delete

                                break;
                            case R.id.action_music: //ok
                                if(!formvalidation())
                                {
                                    break;
                                }

                                for(int i = 0; i < 7; i++)
                                {
                                    if(dayCheckboxes[i].isChecked())
                                    {
                                        String taskName;
                                        if(taskNameCheckbox.isChecked())
                                        {
                                            taskName = customTaskNameEditText.getText().toString();
                                        }
                                        else
                                        {
                                            taskName = taskListView.getSelectedItem().toString();
                                        }
                                        int j = alarmSwitch.isChecked()? 1: 0;

                                        Task temp = new Task(taskName, childId, startPicker.getCurrentHour(), startPicker.getCurrentMinute(),
                                                endPicker.getCurrentHour(), endPicker.getCurrentMinute(), i, j );
                                        db.addTask(childId, temp);
                                        //http://comp4900group23.000webhostapp.com/addTasks.php?email=child@child.ca&groupid=parent@parent.ca&taskName=hihihi&start=12:00:00&end=6:00:00&day=1&alarm=1
                                        final String url ="http://comp4900group23.000webhostapp.com/addTasks.php?email=" + childId
                                                +"&groupid=" + mUsername
                                                +"&taskName=" + Utility.urlEncode(temp.toString())
                                                +"&start=" + temp.getStartTS()
                                                +"&end=" + temp.getEndTS()
                                                +"&day=" + temp.getDay()
                                                +"&alarm="+ temp.getAlarm();
                                        Log.e("url: ", url);
                                        AsyncTask addtaskTask = new AsyncTask()
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
                                                    //save it into query table
                                                }
                                                return null;
                                            }
                                            @Override
                                            protected void onPostExecute(Object o)
                                            {

                                            }

                                        }.execute();
                                    }
                                }
                                finish();
                                break;
                        }
                        return false;
                    }
                });



    }


    private boolean formvalidation()
    {

        return true;
    }

}
