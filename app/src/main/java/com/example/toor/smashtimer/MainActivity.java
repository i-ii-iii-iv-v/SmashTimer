package com.example.toor.smashtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    String[] strings = {"1", "2","3", "4", "5", "6"};
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter(this, R.layout.listview_main, strings);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "" + strings[i], Toast.LENGTH_SHORT).show();
                startTaskActivity();
            }
        });
    }

    private void startTaskActivity()
    {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }

}
