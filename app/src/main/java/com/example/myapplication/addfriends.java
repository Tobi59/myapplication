package com.example.myapplication;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addfriends extends AppCompatActivity {

    Button ButtonAdd;
    EditText maddID;
    EditText maddMail;

    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);

        //instance de l'authentification
        mAuth = FirebaseAuth.getInstance();
        ButtonAdd = findViewById(R.id.add);
        maddID = findViewById(R.id.addID);
        maddMail=findViewById(R.id.addMail);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.add_friends);



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

        ButtonAdd.setOnClickListener(view -> {
            String uID = maddID.getText().toString().trim();
            String Mail = maddMail.getText().toString().trim();
            if(TextUtils.isEmpty(uID)){
                maddID.setError("Remplissez l'ID");
                return;
            }
            if(TextUtils.isEmpty(Mail)){
                maddMail.setError("Remplissez le mail");
                return;
            }
            String ID = maddID.getText().toString();
            DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(ID);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        addFriends(ID);
                    } else {
                        Toast.makeText(addfriends.this, "Clef non valide",
                                Toast.LENGTH_SHORT).show();
                        maddID.setText("");
                        maddMail.setText("");
                    }
                }
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(addfriends.this, "Error lors de la récupération des infos dans la DB",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void addFriends(String ID){
        //*************AJOUT DE L'AMI POUR UTILISATEUR ACTUEL********************
        String mailFriend = maddMail.getText().toString();
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        String uid = currentUser.getUid();//récupère l'ID de l'utilisateur actuel
        DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(uid).child("friends");
        ref.push().setValue(ID);

        //*******************AJOUT L'UTILISATEUR ACTUEL A L'AMI
        String currentMail = currentUser.getEmail();
        DatabaseReference refFriend = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(ID).child("friends");
        refFriend.push().setValue(uid);
        Toast.makeText(addfriends.this, "Ami ajouté !",
                Toast.LENGTH_SHORT).show();
        maddID.setText("");
        maddMail.setText("");
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


