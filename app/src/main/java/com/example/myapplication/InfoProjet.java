package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class InfoProjet extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    private TextView mNom;
    private TextView mDescription;
    private TextView mDatededebut;
    private TextView mDatedefin;
    private TextView mParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_projet);

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
        mNom = findViewById(R.id.nomduProjet);
        mDescription = findViewById(R.id.descriptionduProjet);
        mDatededebut = findViewById(R.id.datededebutduProjet);
        mDatedefin = findViewById(R.id.datedefinduProjet);
        mParticipants = findViewById(R.id.ParticipantsduProjet);

        Intent intent = getIntent();
        Projet projet = (Projet) intent.getSerializableExtra("Projet");
        mNom.setText(projet.getNom());
        mDatededebut.setText(projet.getDatededebut());
        mDatedefin.setText(projet.getDatedefin());
        mDescription.setText(projet.getDescription());
        String texteListe = "";
        for (String element : projet.getParticipants()) {
            texteListe += "- " + element + "\n";
        }

        mParticipants.setText(texteListe);


    }
}