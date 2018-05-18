package com.example.toor.smashtimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class Add_Task_Activity extends AppCompatActivity {

    FloatingActionButton fab;
    String[] listItems = {"eat", "poop", "drink", "sleep", "get dressed", "brush teeth", "sleep", "study"};
    ArrayList<Task> tasklist;
    ArrayAdapter taskAdapter;
    ListView lv;

    EditText customTaskNameEditText;
    Spinner taskListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtaskform);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        taskListView = findViewById(R.id.taskName);
        customTaskNameEditText = findViewById(R.id.customTaskName);
        customTaskNameEditText.setFocusable(false);
        customTaskNameEditText.setEnabled(false);
        customTaskNameEditText.setBackgroundColor(Color.GRAY);
        customTaskNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //if(taskListView)
                //disable spinner
            }
        });



    }



}
