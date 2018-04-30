package com.example.toor.smashtimer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    FloatingActionButton fab;

    ArrayList<Task> tasklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Child");

        tasklist = new ArrayList<Task>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("1", "add task");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main2Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }



}
