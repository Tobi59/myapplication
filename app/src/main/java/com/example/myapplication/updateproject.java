package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;


import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.Map;

public class updateproject extends AppCompatActivity {
    private String id;
    private String nom;
    private String description;
    private String DateDebut;
    private String DateFin;

    private String participants;
    private FirebaseAuth mAuth;

    public updateproject(){

    }

    public updateproject(String id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        //this.DateDebut = DateDebut;
        //this.DateFin = DateFin;
        //this.participants = participants;
    }
    /*public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }
    public String getDateDebut() {
        return DateDebut;
    }
    public String getDateFin() {
        return DateFin;
    }
    public String getParticipants(){
        return participants;
    }
    */

    BottomNavigationView nav;
    //cr??ation de l'instance FireBase


    String[] friends= new String[0];
    private ListView mFriendsListView;
    private ArrayAdapter<String> mFriendsAdapter;
    private Button BoutonUpdate;
    private Button mParticipants_button;

    //Variable li?? au Date
    private String selectedDateStringFin;
    private String selectedDateStringDebut;

    //private Context context;
    private static final String TAG = "AddProjectActivity";
    String projetId;
    DocumentReference projetRefmaj;
    ArrayList<String> Taches;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateproject);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();//r??cup??re les infos de l'utilisteur actuel
        String uid = currentUser.getUid();//r??cup??re l'ID de l'utilisateur actuel
        DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(uid).child("friends");
        //Initialis?? la firebase pour Projets
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projetsRef = db.collection("Projets");

        Intent intent = getIntent();
        Projet projet = (Projet) intent.getSerializableExtra("Projet");
        //id = projet.getId();
        Log.d(TAG, "id "+id);

        //Recuperer le nom du projet
        TextInputLayout textProjectName = findViewById(R.id.Project_Name);
        TextInputEditText textProjectNameEditText = (TextInputEditText) textProjectName.getEditText();
        textProjectNameEditText.setText(intent.getStringExtra("Nom"));
        //Description
        TextInputLayout textDescription = findViewById(R.id.Description);
        TextInputEditText textDescriptionEditText = (TextInputEditText) textDescription.getEditText();
        textProjectNameEditText.setText(intent.getStringExtra("Description"));
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

                // Affichez un calendrier DatePickerDialog pour permettre ?? l'utilisateur de s??lectionner une date
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettez ?? jour votre variable avec la date s??lectionn??e par l'utilisateur
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

                // Affichez un calendrier DatePickerDialog pour permettre ?? l'utilisateur de s??lectionner une date
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettez ?? jour votre variable avec la date s??lectionn??e par l'utilisateur
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
        //Selection des participants
        mFriendsListView = findViewById(R.id.friends_listview);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();

                // Parcourir tous les sous-n??uds de "friends" et stocker les valeurs dans une liste de cha??nes de caract??res
                List<String> friendsValues = new ArrayList<>();
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    String friendValue = friendSnapshot.getValue(String.class);
                    DatabaseReference refFriend = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(friendValue);
                    refFriend.get().addOnCompleteListener(task2 -> {
                        if (!task2.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task2.getException());//debug
                        }
                        else {
                            Map<String, Object> result = (Map<String, Object>) task2.getResult().getValue();
                            String username = (String) result.get("username");
                            System.out.println(username);//debug
                            friendsValues.add(username);
                            friends = friendsValues.toArray(new String[0]);
                            System.out.println(Arrays.toString(friends));
                            mFriendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, friends);
                            mFriendsListView.setAdapter(mFriendsAdapter);
                        }
                    });
                }
            } else {
                Log.e("firebase", "Error getting data", task.getException());
            }
        });
        mFriendsListView.setAdapter(mFriendsAdapter);
        mFriendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, friends);
        mFriendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        BoutonUpdate = findViewById(R.id.buttonuptade);
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
        BoutonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userProjectName = textProjectNameEditText.getText().toString();
                String userDescription = textDescriptionEditText.getText().toString();
                ArrayList<String> selectedFriends = new ArrayList<>();
                for (int i = 0; i < mFriendsAdapter.getCount(); i++) {
                    if (mFriendsListView.isItemChecked(i)) {
                        selectedFriends.add(mFriendsAdapter.getItem(i));
                    }
                }
                String selectedParticipantsString = selectedFriends.toString();
                //Creer le projet dans la databse
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("Nom", textProjectNameEditText.getText().toString());
                updateData.put("Description", textDescriptionEditText.getText().toString());
                updateData.put("DatedeDebut", selectedDateStringDebut);
                updateData.put("DatedeFin", selectedDateStringFin);
                updateData.put("Participants", selectedParticipantsString);


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Log.d(TAG,"id"+id);
                DocumentReference docRef = db.collection("Projets").document(String.valueOf(id));

                docRef.update(updateData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // La mise ?? jour a ??t?? effectu??e avec succ??s
                                Log.d(TAG, "succes");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // La mise ?? jour a ??chou??
                                Log.d(TAG, "echec");
                            }
                        });

            }




        });


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //        this, android.R.layout.simple_dropdown_item_1line, friends);
        //AutoCompleteTextView textView = findViewById(R.id.ListeParticipants);
        //textView.setAdapter(adapter);

        //instance de l'authentification
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
    }
    @Override
    public void onStart() {
        super.onStart();

        // v??rifie que l'utilisateur n'est pas connect??, mets ?? jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//r??cup??re les infos de l'utilisteur actuel
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }

}