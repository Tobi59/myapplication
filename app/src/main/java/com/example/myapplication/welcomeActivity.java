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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcomeActivity extends AppCompatActivity {

    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //instance de l'authentification
        mAuth = FirebaseAuth.getInstance();

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
                    //le return false devrait pas être dans le default ?
                    return false;
            }
        });
    }
    //class qui vérifie si l'utilisateur n'est pas déjà connecté
    @Override
    public void onStart() {
        super.onStart();
        // vérifie que l'utilisateur n'est pas connecté, mets à jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }
}
