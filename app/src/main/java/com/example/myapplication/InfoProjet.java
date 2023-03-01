package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InfoProjet extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    private TextView mNom;
    private TextView mDescription;
    private TextView mDatededebut;
    private TextView mDatedefin;
    private TextView mParticipants;
    private RecyclerView mRecyclerViewtache;
    private TacheAdapter tachesAdapter;
    private List<Tache> mTacheList = new ArrayList<>();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String id;
    private Button updateprojet;
    private Button supprbouton;
    String projetId;
    private List<String> listetaches;
    private static final String TAG = "AddProjectActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_projet);

        Intent intent = getIntent();
        Projet projet = (Projet) intent.getSerializableExtra("Projet");


        Log.d(TAG, "id : " + projet.getId());
        projetId = projet.getId();
        listetaches = projet.getTaches();
        supprbouton = findViewById(R.id.supprimerprojet);
        supprbouton.setOnClickListener(new View.OnClickListener() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            @Override
            public void onClick(View view) {
                if (listetaches != null){
                    for (int i=0; i < listetaches.size();i++){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Taches").document(listetaches.get(i))
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Projet supprimé avec succès !");
                                        startActivity(new Intent(getApplicationContext(), welcomeActivity.class));
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InfoProjet.this, "Tache supprimé !",
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
                }
                db.collection("Projets").document(projetId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Projet supprimé avec succès !");
                                startActivity(new Intent(getApplicationContext(), welcomeActivity.class));
                                overridePendingTransition(0, 0);
                                Toast.makeText(InfoProjet.this, "Projet supprimé !",
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


        updateprojet = findViewById(R.id.boutonuptadeproject);
        updateprojet.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),updateproject.class));
                overridePendingTransition(0,0);
            }
        }));

        //Systeme de navigation entre les pages
        mAuth = FirebaseAuth.getInstance();
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.home);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), welcomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add_friends:
                        startActivity(new Intent(getApplicationContext(), addfriends.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add_project:
                        startActivity(new Intent(getApplicationContext(), CreationTachesetProjet.class));
                        overridePendingTransition(0, 0);
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


        mNom.setText(projet.getNom());
        mDatededebut.setText(projet.getDatededebut());
        mDatedefin.setText(projet.getDatedefin());
        mDescription.setText(projet.getDescription());
        projet.getId();
        String texteListe = "";
        for (String element : projet.getParticipants()) {
            texteListe += "- " + element + "\n";
        }

        mParticipants.setText(texteListe);

        mRecyclerViewtache = findViewById(R.id.recycler_viewtache);
        mRecyclerViewtache.setHasFixedSize(true);
        mRecyclerViewtache.setLayoutManager(new LinearLayoutManager(InfoProjet.this));

        List<Tache> listeDeTaches = new ArrayList<>();
        TacheAdapter adapter = new TacheAdapter(InfoProjet.this, listeDeTaches, new TacheAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tache tache) {
                Intent intent = new Intent(InfoProjet.this, InfoTache.class);
                intent.putExtra("Tache", tache);
                startActivity(intent);
            }

        });
        mRecyclerViewtache.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> idTaches = projet.getTaches();// Récupère la liste des id des tâches du projet
        if (idTaches == null) {
            //on fait rien
        }
        else {
            db.collection("Taches")
                    .whereIn("id", idTaches) // Filtre les tâches en fonction des id
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Tache tache = documentSnapshot.toObject(Tache.class);
                                listeDeTaches.add(tache);
                                Log.d(TAG, "liste taches : " + listeDeTaches);

                            }
                            adapter.notifyDataSetChanged(); // rafraîchit l'affichage de la RecyclerView
                        }
                    });
        }


    }



    }