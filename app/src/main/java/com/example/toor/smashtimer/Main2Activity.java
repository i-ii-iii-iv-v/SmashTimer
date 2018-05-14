package com.example.toor.smashtimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    FloatingActionButton fab;
    String[] listItems = {"eat", "poop", "drink", "sleep", "get dressed", "brush teeth", "sleep", "study"};
    ArrayList<Task> tasklist;
    ArrayAdapter taskAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        Intent intent = getIntent();

        String childName = intent.getStringExtra("Child");
        final String childId = intent.getStringExtra("childid");
        Log.d("1", childId.toString());
        tasklist = new ArrayList<Task>();

        /*
        testing purpose
         */



        lv = (ListView) findViewById(R.id.taskListView2);
        taskAdapter = new ArrayAdapter(this, R.layout.listview_main, tasklist);
        lv.setAdapter(taskAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main2Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
                //mBuilder.setTitle("Add Tssk");

                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                final TimePicker startPicker = (TimePicker)mView.findViewById(R.id.simpleTimePicker);
                final TimePicker endPicker = (TimePicker)mView.findViewById(R.id.endTimePicker);
               ArrayAdapter<String> strAdapter = new ArrayAdapter<String>(Main2Activity.
                        this, android.R.layout.simple_spinner_item, listItems);
                strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               mSpinner.setAdapter(strAdapter);

                mBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if(mSpinner.getSelectedItem().toString().equalsIgnoreCase("")){
                            //
                        //}
                        //https://notifood.000webhostapp.com/addtask.php?username=aa&password=aa&task=eat%20lunch&startTime=6:30:00&endTime=7:00:00&childid=bb
                        String requesturl = "https://notifood.000webhostapp.com/addtask.php?username=aa&password=aa&task=";
                        requesturl+= mSpinner.getSelectedItem().toString().replace(" ", "%20");
                        requesturl+= "&startTime=" + startPicker.getCurrentHour().toString() +":"+startPicker.getCurrentMinute().toString()+":00&endTime=";
                        requesturl+= endPicker.getCurrentHour().toString() +":"+endPicker.getCurrentMinute().toString()+":00&childid="+childId;
                        final String url = requesturl;
                        Log.e("1", url);
                        AsyncTask asyncTask = new AsyncTask()
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
                                Log.e("1", o.toString());

                            }

                        }.execute();
                        //tasklist.add(new Task(mSpinner.getSelectedItem().toString()));
                        taskAdapter.notifyDataSetChanged();
                        //lv.invalidateViews();
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

    public void updateList()
    {

    }


}
