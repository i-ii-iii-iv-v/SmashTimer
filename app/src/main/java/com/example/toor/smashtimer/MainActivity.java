package com.example.toor.smashtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cl = new ArrayList<Child>(); //initialize list

        //--> test case
        cl.add(new Child("Alex"));
        cl.add(new Child("David"));
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
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("Child", cl.get(i).toString());
        startActivity(intent);
    }

}
