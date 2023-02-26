package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class addtasks extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    private int id;
    String[] friends = {"Alice", "Bob", "Charlie", "Dave", "Eve", "Frank"};
    private ListView mFriendsListView;
    private ArrayAdapter<String> mFriendsAdapter;
    private Button BoutonCreation;
    private Button mParticipants_button;
    private String selectedDateStringFin;
    private String selectedDateStringDebut;

    //Variable Status
    String[] status = {"Non commencé","En cours", "Fini"};
    private ListView mStatusListView;
    private ArrayAdapter<String> mStatusAdapter;
    private Button mStatus_button;
    private String selectedStatus;

    //Variable Projet

    private ListView mProjetListView;
    private ArrayAdapter<String> mProjetAdapter;
    private Button mProjet_button;
    List<String> Nomdeprojet = new ArrayList<>();
    private String selectedProjet;
    String projetId;
    String tacheId;
    DocumentReference projetRefmaj;


    private static final String TAG = "AddTachesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtasks);
        //Initialisation de la Firebase pour Taches
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference TachesRef = db.collection("Taches");

        //Systeme de navigation entre les pages
        mAuth = FirebaseAuth.getInstance();
        nav=findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.add_project);
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
        //Code lié a la recuperation des projets existants pour le choix du projet
        CollectionReference projetsRef = db.collection("Projets");

        // Récupérez tous les documents de la collection de "projets"
        projetsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Parcourez tous les documents dans le QuerySnapshot
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Vérifiez si le document existe
                    if (documentSnapshot.exists()) {
                        // Récupérez la valeur du champ "Nomp" pour chaque document
                        String Nom = documentSnapshot.getString("Nom ");

                        // Ajoutez la valeur du champ à la liste
                        Nomdeprojet.add(Nom);
                        Log.d(TAG, "Nom du projet : " + Nom);
                    } else {
                        Log.d(TAG, "Aucun document trouvé pour cet ID");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Erreur lors de la récupération des documents", e);
            }
        });


        //Recuperer le nom de la tache
        TextInputLayout textTacheName = findViewById(R.id.Tache_Nom);
        TextInputEditText textTacheNameEditText = (TextInputEditText) textTacheName.getEditText();
        //Description
        TextInputLayout textDescription = findViewById(R.id.Description);
        TextInputEditText textDescriptionEditText = (TextInputEditText) textDescription.getEditText();
        //Systeme de Choix de projets
        mProjetListView = findViewById(R.id.choixprojet_listview);
        mProjetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Nomdeprojet);
        mProjetListView.setAdapter(mProjetAdapter);
        mProjetListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Systeme de Participants
        mFriendsListView = findViewById(R.id.friends_listview);
        mFriendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, friends);
        mFriendsListView.setAdapter(mFriendsAdapter);
        mFriendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //Systeme de Status
        mStatusListView = findViewById(R.id.status_listview);
        mStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, status);
        mStatusListView.setAdapter(mStatusAdapter);
        mStatusListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //la date de debut
        Button Datededebut =findViewById(R.id.Datededebut);
        final Context context = this;
        final Calendar selectedDateDebut = Calendar.getInstance();

        Datededebut.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                // Obtenez la date actuelle
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Affichez un calendrier DatePickerDialog pour permettre à l'utilisateur de sélectionner une date
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettez à jour votre variable avec la date sélectionnée par l'utilisateur
                        selectedDateDebut.set(Calendar.YEAR, year);
                        selectedDateDebut.set(Calendar.MONTH, month);
                        selectedDateDebut.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Date DateChoisieDebut = selectedDateDebut.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        selectedDateStringDebut = dateFormat.format(DateChoisieDebut);

                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        //la date de fin
        Button Datedefin = findViewById(R.id.Datedefin);
        final Calendar selectedDateFin = Calendar.getInstance();

        Datedefin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenez la date actuelle
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Affichez un calendrier DatePickerDialog pour permettre à l'utilisateur de sélectionner une date
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettez à jour votre variable avec la date sélectionnée par l'utilisateur
                        selectedDateFin.set(Calendar.YEAR, year);
                        selectedDateFin.set(Calendar.MONTH, month);
                        selectedDateFin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Date DateChoisieFin = selectedDateFin.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        selectedDateStringFin = dateFormat.format(DateChoisieFin);

                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        //Visibilité de la liste quand on appuie sur Choix du Projet
        mProjet_button = findViewById(R.id.choixprojet_button);
        mProjet_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProjetListView.getVisibility() == View.GONE) {
                    mProjetListView.setVisibility(View.VISIBLE);
                } else {
                    mProjetListView.setVisibility(View.GONE);
                }
            }
        }));
        //Visibilité de la liste quand on appuie sur Selectionnez les participants
        mParticipants_button = findViewById(R.id.participants_button);
        mParticipants_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFriendsListView.getVisibility() == View.GONE) {
                    mFriendsListView.setVisibility(View.VISIBLE);
                } else {
                    mFriendsListView.setVisibility(View.GONE);
                }
            }
        }));
        //Visibilité de la liste quand on appuie sur Status
        mStatus_button = findViewById(R.id.status_button);
        mStatus_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStatusListView.getVisibility() == View.GONE) {
                    mStatusListView.setVisibility(View.VISIBLE);
                } else {
                    mStatusListView.setVisibility(View.GONE);
                }
            }
        }));
        BoutonCreation = findViewById(R.id.button_creation);
        BoutonCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mProjetAdapter.getCount(); i++) {
                    if (mProjetListView.isItemChecked(i)) {
                        selectedProjet = mProjetAdapter.getItem(i);
                    }
                }
                String nomProjet = selectedProjet;
                Log.d(TAG, "Le nom du projet est : "+ nomProjet );
                Query query = projetsRef.whereEqualTo("Nom ", nomProjet).limit(1);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                projetId = documentSnapshot.getId();
                                Log.d(TAG, "ID du projet " + nomProjet + " : " + projetId);
                            } else {
                                Log.d(TAG, "Aucun document trouvé pour ce nom de projet");
                            }
                        } else {
                            Log.d(TAG, "Erreur : ", task.getException());
                        }
                    }
                });


                for (int i = 0; i < mStatusAdapter.getCount(); i++) {
                    if (mStatusListView.isItemChecked(i)) {
                        selectedStatus = mStatusAdapter.getItem(i);
                    }
                }
                ArrayList<String> selectedFriends = new ArrayList<>();
                for (int i = 0; i < mFriendsAdapter.getCount(); i++) {
                    if (mFriendsListView.isItemChecked(i)) {
                        selectedFriends.add(mFriendsAdapter.getItem(i));
                    }
                }
                String selectedParticipantsString = selectedFriends.toString();
                //Creer la tache dans la databse
                Map<String, Object> TacheMap = new HashMap<>();
                TacheMap.put("ID  ", id);
                TacheMap.put("Nom ", textTacheNameEditText.getText().toString());
                TacheMap.put("Description ", textDescriptionEditText.getText().toString());
                TacheMap.put("Date de Debut ", selectedDateStringDebut );
                TacheMap.put("Date de Fin ", selectedDateStringFin );
                TacheMap.put("Status ", selectedStatus );
                TacheMap.put("Participants ",selectedParticipantsString);

                TachesRef.add(TacheMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Tache ajouté avec l'ID : " + documentReference.getId());
                                tacheId = documentReference.getId();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Erreur lors de l'ajout de la tache", e);
                            }
                        });
                //Ajout de l'ID de la tache dans le projet

                projetRefmaj = db.collection("Projets").document(String.valueOf(projetId));
                List<String> TachesId = new ArrayList<String>();
                String Taches = "Taches";
                TachesId.add(tacheId);
                projetRefmaj.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        // Le document existe déjà, récupérer les ID existantes ou  créer ID s'il n'existe pas encore
                        List<String> TachesIdExistantes = (List<String>) document.get(Taches);
                        if (TachesIdExistantes == null) {
                            TachesIdExistantes = new ArrayList<>();
                        }
                        TachesIdExistantes.addAll(TachesId);
                        projetRefmaj.update(Taches, TachesIdExistantes)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Champ " + Taches + " mis à jour avec succès!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Erreur lors de la mise à jour du champ " + Taches, e));
                    }
                    else {
                        Log.w(TAG, "Erreur lors de la récupération du document", task.getException());
                    }
                });


            }



        });




    }
    @Override
    public void onStart() {
        super.onStart();
        // vérifie que l'utilisateur n'est pas connecté, mets à jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }
}