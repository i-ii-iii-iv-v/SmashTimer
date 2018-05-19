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
import android.widget.RelativeLayout;
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


    private EditText customTaskNameEditText;
    private Spinner taskListView;
    private CheckBox taskNameCheckbox;
    private CheckBox[] dayCheckboxes;
    private TimePicker startPicker;
    private TimePicker endPicker;
    private Switch alarmSwitch;
    private RelativeLayout customTaskLayout;
    private DatabaseHelper db;
    private TextView spinnerError;
    private TextView dayError;

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

        customTaskLayout = findViewById(R.id.customTaskLayout);
        customTaskLayout.setBackgroundColor(Color.LTGRAY);

        dayError = findViewById(R.id.dayError);
        dayCheckboxes = new CheckBox[7];
        dayCheckboxes[0] = findViewById(R.id.mBox);
        dayCheckboxes[1] = findViewById(R.id.tuBox);
        dayCheckboxes[2] = findViewById(R.id.wBox);
        dayCheckboxes[3] = findViewById(R.id.thBox);
        dayCheckboxes[4] = findViewById(R.id.fBox);
        dayCheckboxes[5] = findViewById(R.id.saBox);
        dayCheckboxes[6] = findViewById(R.id.suBox);

        alarmSwitch = findViewById(R.id.alarmSwitch);

        spinnerError = findViewById(R.id.spinnerError);
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
                    customTaskLayout.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    customTaskNameEditText.setEnabled(false);
                    customTaskLayout.setBackgroundColor(Color.LTGRAY);

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
                                finish();
                                break;
                            case R.id.action_schedules: //delete

                                break;
                            case R.id.action_music: //ok
                                if(formvalidation() == false)
                                {
                                    break;
                                }

                                for(int i = 0; i < 7; i++) {
                                    if (dayCheckboxes[i].isChecked()) {
                                        String taskName;
                                        if (taskNameCheckbox.isChecked()) {
                                            taskName = customTaskNameEditText.getText().toString();
                                        } else {
                                            taskName = taskListView.getSelectedItem().toString();
                                        }
                                        int j = alarmSwitch.isChecked() ? 1 : 0;

                                        Task temp = new Task(taskName, childId, startPicker.getCurrentHour(), startPicker.getCurrentMinute(),
                                                endPicker.getCurrentHour(), endPicker.getCurrentMinute(), i, j);
                                        db.addTask(childId, temp);
                                        final String url = WebService.addTask(mUsername, temp);

                                        AsyncTask addtaskTask = new AsyncTask() {
                                            @Override
                                            protected Object doInBackground(Object[] objects) {
                                                OkHttpClient client = new OkHttpClient();
                                                Request request = new Request.Builder().url(url).build();

                                                Response response = null;

                                                try {
                                                    response = client.newCall(request).execute();
                                                    return response.body().string();

                                                } catch (IOException e) {
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
        boolean flag = true;
        if(taskNameCheckbox.isChecked())
        {
            if(customTaskNameEditText.getText().toString().equals(""))
            {
                customTaskNameEditText.setError("Enter task name");
                flag = false;
            }
        }
        else
        {
            if(taskListView.getSelectedItem().toString().equals("Choose a task"))
            {
                spinnerError.setError("Select a Task");
                flag = false;
            }
        }

        int count = 0;
        for(int i = 0; i < 7; i++)
        {
            if(dayCheckboxes[i].isChecked())
                count++;
        }

        if(count == 0)
        {
            dayError.setError("Please select desired days");
            flag = false;
        }

        return flag;
    }

}
