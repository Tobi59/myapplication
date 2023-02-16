package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class welcomeActivity extends AppCompatActivity {


    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        


        nav=findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.home);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        return true;


                            case R.id.settings:
                                startActivity(new Intent(getApplicationContext(),settings.class));
                                overridePendingTransition(0,0);
                                return true;
                                    case R.id.add_friends:
                                        startActivity(new Intent(getApplicationContext(),addfriends.class));
                                        overridePendingTransition(0,0);
                                        return true;
                                            case R.id.add_project:
                                                startActivity(new Intent(getApplicationContext(),addproject.class));
                                                overridePendingTransition(0,0);
                                                return true;
                                            default:

                                        }
                                        return false;
            }
        });
    }
}
