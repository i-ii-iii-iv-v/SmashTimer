package com.example.toor.smashtimer;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {

    private TextView textFavorites;
    private TextView textSchedules;
    private TextView textMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtaskform);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites: //cancel
                                textFavorites.setVisibility(View.VISIBLE);
                                textSchedules.setVisibility(View.GONE);
                                textMusic.setVisibility(View.GONE);
                                break;
                            case R.id.action_schedules: //delete
                                textFavorites.setVisibility(View.GONE);
                                textSchedules.setVisibility(View.VISIBLE);
                                textMusic.setVisibility(View.GONE);
                                break;
                            case R.id.action_music: //ok
                                textFavorites.setVisibility(View.GONE);
                                textSchedules.setVisibility(View.GONE);
                                textMusic.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }
                });
    }
}


