package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {

    BottomNavigationView nav;
    Button mdecoButton;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //instance de l'authentification
        mAuth = FirebaseAuth.getInstance();
        mdecoButton = findViewById(R.id.deconnexionButton);
        nav=findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.settings);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
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
        //méthode de déconnexion
        mdecoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                if(mAuth.getCurrentUser()==null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
    }
    public void onStart() {
        super.onStart();
        // vérifie que l'utilisateur n'est pas connecté, mets à jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }
}
