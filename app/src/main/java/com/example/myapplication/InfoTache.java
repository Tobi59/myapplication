package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class InfoTache extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    private TextView mNom;
    private TextView mDescription;
    private TextView mDatededebut;
    private TextView mDatedefin;
    private TextView mParticipants;
    private TextView mStatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tache);

        //Systeme de navigation entre les pages
        mAuth = FirebaseAuth.getInstance();
        nav=findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.home);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
                        overridePendingTransition(0,0);
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
                        startActivity(new Intent(getApplicationContext(),CreationTachesetProjet.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:

                }
                return false;
            }
        });
        mNom = findViewById(R.id.nomdelaTache);
        mDescription = findViewById(R.id.descriptiondelaTache);
        mDatededebut = findViewById(R.id.datededebutdelaTache);
        mDatedefin = findViewById(R.id.datedefindelaTache);
        mParticipants = findViewById(R.id.ParticipantsdelaTache);
        mStatus = findViewById(R.id.statusdelaTache);

        Intent intent = getIntent();
        Tache tache = (Tache) intent.getSerializableExtra("Tache");
        mNom.setText(tache.getNom());
        mStatus.setText(tache.getStatus());
        mDatededebut.setText(tache.getDatededebut());
        mDatedefin.setText(tache.getDatedefin());
        mDescription.setText(tache.getDescription());
        String texteListe = "";
        for (String element : tache.getParticipants()) {
            texteListe += "- " + element + "\n";
        }

        mParticipants.setText(texteListe);


    }
}