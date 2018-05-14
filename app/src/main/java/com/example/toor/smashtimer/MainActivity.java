package com.example.toor.smashtimer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;

public class MainActivity extends AppCompatActivity {

    /*
    layout to create list ui
     */
    ListView lv;

    /*
    Contains list of child added by the user
     */
    ArrayList<Child> cl;

    /*
    makes list based on cl arraylist
     */
    ArrayAdapter adapter;
    SQLiteDatabase SQLdb;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!DatabaseHelper.checkDatabase(getApplicationContext()))
        {
            db = new DatabaseHelper(getApplicationContext());
        }

        SQLdb = SQLiteDatabase.openDatabase(getApplicationContext().getDatabasePath(DatabaseHelper.DATABASE_NAME).toString(), null, OPEN_READWRITE, null);
        DatabaseHelper.testQueries(SQLdb);

        cl = new ArrayList<Child>(); //initialize list

        //--> test case
        cl.add(new Child("Alex", "bb", "aa@aa.com"));
        cl.add(new Child("David", "bb", "bb@bb.com"));
        //-->

        //make list
        lv = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, R.layout.listview_main, cl);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startTaskActivity(i);
            }
        });
    }

    /*
    go to new activity based on item selected from the list. param i is the index of item selected
     */
    private void startTaskActivity(int i)
    {
        Intent intent = new Intent(this, Main3Activity.class);
        Log.e("main", cl.get(i).getid());
        intent.putExtra("Child", cl.get(i).toString());
        intent.putExtra("childid", cl.get(i).getid());
        startActivity(intent);
    }

}
