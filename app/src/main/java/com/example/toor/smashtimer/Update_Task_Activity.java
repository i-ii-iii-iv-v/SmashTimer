package com.example.toor.smashtimer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Update_Task_Activity extends AppCompatActivity {
    FloatingActionButton fab;
    String[] listItems = {"Choose a task", "eat", "poop", "drink", "sleep", "get dressed", "brush teeth", "sleep", "study"};


    private EditText customTaskNameEditText;
    private Spinner taskListView;
    private CheckBox taskNameCheckbox;
    private TimePicker startPicker;
    private TimePicker endPicker;
    private Switch alarmSwitch;
    private RelativeLayout customTaskLayout;
    private DatabaseHelper db;
    private TextView spinnerError;

    private String mUsername;
    private String childId;


    private String pTaskName;
    private int pStartHour;
    private int pStartMinute;
    private int pEndHour;
    private int pEndMinute;
    private int pDay;
    private int pAlarm;
    Context context;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__task_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = this;
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        pTaskName = intent.getStringExtra("pTaskName");
        pStartHour = Integer.parseInt(intent.getStringExtra("pStartHour"));
        pStartMinute = Integer.parseInt(intent.getStringExtra("pStartMinute"));
        pEndHour = Integer.parseInt(intent.getStringExtra("pEndHour"));
        pEndMinute = Integer.parseInt(intent.getStringExtra("pEndMinute"));
        pDay = Integer.parseInt(intent.getStringExtra("pDay"));
        pAlarm = Integer.parseInt(intent.getStringExtra("pAlarm"));

        childId = intent.getStringExtra("childId");
        mUsername = intent.getStringExtra("username");


        startPicker = findViewById(R.id.startTimePicker);
        startPicker.setHour(pStartHour);
        startPicker.setMinute(pStartMinute);


        endPicker = findViewById(R.id.endTimePicker);
        endPicker.setHour(pEndHour);
        endPicker.setMinute(pEndMinute);

        customTaskNameEditText = findViewById(R.id.customTaskName);
        customTaskNameEditText.setEnabled(true);
        customTaskNameEditText.setText(pTaskName);
        customTaskNameEditText.clearFocus();

        alarmSwitch = findViewById(R.id.alarmSwitch);
        if(pAlarm ==1)
            alarmSwitch.setChecked(true);

        spinnerError = findViewById(R.id.spinnerError);
        taskListView = findViewById(R.id.taskName);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, listItems);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskListView.setAdapter(spinnerArrayAdapter);

        taskNameCheckbox = findViewById(R.id.taskNameCheckBox);
        taskNameCheckbox.setChecked(true);
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

                                AsyncTask deleteTask = new AsyncTask()
                                {
                                    Task task = new Task(pTaskName, childId, pStartHour, pStartMinute ,pEndHour, pEndMinute, pDay, pAlarm);
                                    final String deleteTaskurl = WebService.removeTask(task, mUsername);

                                    @Override
                                    protected void onPreExecute()
                                    {
                                        db.removeTask(task);
                                    }
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
                                        }
                                        return null;
                                    }
                                    @Override
                                    protected void onPostExecute(Object o)
                                    {
                                        String status;
                                        try
                                        {
                                            if(o == null)
                                            {
                                                return;
                                            }
                                            JSONObject returnData = new JSONObject(o.toString());
                                            status = returnData.getString("status");
                                            if(status.equalsIgnoreCase("pass"))
                                            {
                                                return;
                                                //handle errors:
                                            }

                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }

                                }.execute();

                                if(formvalidation() == false)
                                {
                                    break;
                                }
                                String taskName;
                                if (taskNameCheckbox.isChecked())
                                {
                                    taskName = customTaskNameEditText.getText().toString();
                                }
                                else
                                {
                                    taskName = taskListView.getSelectedItem().toString();
                                }
                                int j = alarmSwitch.isChecked() ? 1 : 0;

                                Task temp = new Task(taskName, childId, startPicker.getCurrentHour(), startPicker.getCurrentMinute(),
                                        endPicker.getCurrentHour(), endPicker.getCurrentMinute(), pDay, j);
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
                                        String status;
                                        try
                                        {
                                            if(o == null)
                                            {
                                                return;
                                            }
                                            JSONObject returnData = new JSONObject(o.toString());
                                            status = returnData.getString("status");
                                            if(status.equalsIgnoreCase("pass"))
                                            {
                                                return;
                                                //handle errors:
                                            }

                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }

                                }.execute();

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

        return flag;
    }
}
