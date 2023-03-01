package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;

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
    private String tacheId;
    private String projetId;
    private Button supprbouton;
    private Button updatetache;
    private static final String TAG = "AddProjectActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tache);
        Intent intent = getIntent();
        Tache tache = (Tache) intent.getSerializableExtra("Tache");
        Projet projet = (Projet) intent.getSerializableExtra("Projet");
        projetId = projet.getId();
        tacheId = tache.getId();

        supprbouton = findViewById(R.id.supprimerprojet);
        supprbouton.setOnClickListener(new View.OnClickListener() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            @Override
            public void onClick(View view) {
                DocumentReference projetRef = db.collection("Projets").document(projetId);

                projetRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> taches = (List<String>) documentSnapshot.get("Taches");
                            for (int i=0; i < taches.size();i++){
                                    if(taches.get(i) == tacheId){
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("Taches" + i, FieldValue.delete());
                                        projetRef.update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Tache supprimée avec succès !");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Erreur lors de la suppression de la tache", e);
                                                    }
                                                });
                                    }

                        }
                        } else {
                            // Le document n'existe pas
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de la récupération du document
                    }
                });
                db.collection("Taches").document(tacheId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Projet supprimé avec succès !");
                                startActivity(new Intent(getApplicationContext(), welcomeActivity.class));
                                overridePendingTransition(0, 0);
                                Toast.makeText(InfoTache.this, "Tache supprimé !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Erreur lors de la suppression du projet", e);
                            }
                        });
            }
        });

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