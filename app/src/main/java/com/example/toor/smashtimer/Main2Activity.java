package com.example.toor.smashtimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
        String name = intent.getStringExtra("Child");

        tasklist = new ArrayList<Task>();

        /*
        testing purpose
         */

        tasklist.add(new Task("wash hands"));
        tasklist.add(new Task("brush teeth"));

        lv = (ListView) findViewById(R.id.taskListView);
        taskAdapter = new ArrayAdapter(this, R.layout.listview_main, tasklist);
        lv.setAdapter(taskAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main2Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
                //mBuilder.setTitle("Add Tssk");

                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
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
                        tasklist.add(new Task(mSpinner.getSelectedItem().toString()));
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
