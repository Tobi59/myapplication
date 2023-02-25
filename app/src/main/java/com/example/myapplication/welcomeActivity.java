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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class welcomeActivity extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
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
                         startActivity(new Intent(getApplicationContext(),CreationTachesetProjet.class));
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
                String username = (String) result.get("username");
                System.out.println(username);//debug
                Toast.makeText(welcomeActivity.this, "Bienvenue "+username+" !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }
}
