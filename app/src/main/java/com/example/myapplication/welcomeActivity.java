package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
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

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(welcomeActivity.this, "Home", Toast.LENGTH_LONG).show();

                        switch (item.getItemId()) {
                            case R.id.settings:
                                Toast.makeText(welcomeActivity.this, "Settings", Toast.LENGTH_LONG).show();

                                switch (item.getItemId()) {
                                    case R.id.add_friends:
                                        Toast.makeText(welcomeActivity.this, "Add Friends", Toast.LENGTH_LONG).show();

                                        switch (item.getItemId()) {
                                            case R.id.add_project:
                                                Toast.makeText(welcomeActivity.this, "Add Projects", Toast.LENGTH_LONG).show();

                                            default:

                                        }


                                }
                        }
                }
                return true;
            }
        });
    }
}
