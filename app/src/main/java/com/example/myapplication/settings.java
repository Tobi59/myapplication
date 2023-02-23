package com.example.myapplication;

import static com.example.myapplication.register.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class settings extends AppCompatActivity {

    BottomNavigationView nav;
    Button mdecoButton;
    Button mChangeButton;
    Button mChangeButtonMDP;
    Button mChangeButtonUsername;
    EditText mnewEmail;
    EditText mnewMDP;
    EditText mnewUsername;
    private FirebaseAuth mAuth; //variable qui stockera l'instance de l'authentification
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();//instance de l'authentification
        mdecoButton = findViewById(R.id.deconnexionButton);//bouton déco
        mChangeButton = findViewById(R.id.ChangeButton);//bouton maj email
        mChangeButtonMDP = findViewById(R.id.ChangeButtonMDP);//bouton maj email
        mChangeButtonUsername = findViewById(R.id.ChangeButtonUsername);//bouton maj email
        mnewEmail = findViewById(R.id.newMail);
        mnewMDP = findViewById(R.id.newMDP);
        mnewUsername = findViewById(R.id.newUsername);
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
        mdecoButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            if(mAuth.getCurrentUser()==null){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        //méthode chgt de mail
        mChangeButton.setOnClickListener(view ->{
            String newEmail = mnewEmail.getText().toString();
            updateEmail(newEmail);
        });
        //méthode chgt de MDP
        mChangeButtonMDP.setOnClickListener(view ->{
            String newMDP = mnewMDP.getText().toString();
            updatePassword(newMDP);
        });
        //méthode chgt de Username
        mChangeButtonUsername.setOnClickListener(view ->{
            String newUsername = mnewUsername.getText().toString();
            updateUsername(newUsername);
        });
    }
    //fonction pour mettre à jour l'adresse email
    public void updateEmail(String mail) {
        Map<String, Object> majDB = new HashMap<>();// map qui stocke les infos de la db
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        String uid = currentUser.getUid();//récupère l'ID de l'utilisateur actuel
        DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(uid);
        //méthode pour récupérer le username dans la db afin de mettre à jour la DB
        ref.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());//debug
            }
            else {
                // Récupérer le résultat sous forme de Map<String, Object>
                Map<String, Object> result = (Map<String, Object>) task.getResult().getValue();
                String username = (String) result.get("username");
                majDB.put("username", username);
            }
        });
        // Ajoutez les nouvelles valeurs en utilisant les clés de votre choix
        majDB.put("email", mail);
        majDB.put("UID", uid);
        currentUser.updateEmail(mail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                        // Utilisez la méthode updateChildren() pour mettre à jour l'enfant avec les nouvelles valeurs
                        ref.updateChildren(majDB);
                        Toast.makeText(settings.this, "Email changé !",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.w(TAG, "updateUserFailure", task.getException());
                        Toast.makeText(settings.this, "Reconnectez vous pour faire cette opération",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //fonction pour mettre à jour l'adresse email
    public void updatePassword(String password) {
        Map<String, Object> majDB = new HashMap<>();// map qui stocke les infos de la db
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        String uid = currentUser.getUid();//récupère l'ID de l'utilisateur actuel
        DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(uid);
        //méthode pour récupérer le username dans la db afin de mettre à jour la DB
        ref.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());//debug
            }
            else {
                // Récupérer le résultat sous forme de Map<String, Object>
                Map<String, Object> result = (Map<String, Object>) task.getResult().getValue();
                String username = (String) result.get("username");
                String email = (String) result.get("email");
                majDB.put("username", username);
                majDB.put("email", email);
            }
        });
        majDB.put("UID", uid);
        currentUser.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User MDP updated.");
                        // Utilisez la méthode updateChildren() pour mettre à jour l'enfant avec les nouvelles valeurs
                        ref.updateChildren(majDB);
                        Toast.makeText(settings.this, "MDP changé !",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.w(TAG, "updateUserFailure", task.getException());
                        Toast.makeText(settings.this, "Reconnectez vous pour faire cette opération",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void updateUsername(String username){
        Map<String, Object> enfantUpdates = new HashMap<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        String uid = currentUser.getUid();//récupère l'ID de l'utilisateur actuel
        DatabaseReference ref = FirebaseDatabase.getInstance(" https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(uid);
        ref.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());//debug
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));//debug
                // Récupérer le résultat sous forme de Map<String, Object>
                Map<String, Object> result = (Map<String, Object>) task.getResult().getValue();
                // Extraire la valeur de "username"
                String email = (String) result.get("email");
                enfantUpdates.put("email", email);
            }
        });
        enfantUpdates.put("UID", uid);
        enfantUpdates.put("username", username);
        // Utilisez la méthode updateChildren() pour mettre à jour l'enfant avec les nouvelles valeurs
        ref.updateChildren(enfantUpdates);
        Toast.makeText(settings.this, "Username changé !",
                Toast.LENGTH_SHORT).show();
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        // vérifie que l'utilisateur est connecté, mets à jour l'UI si besoin
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }
}