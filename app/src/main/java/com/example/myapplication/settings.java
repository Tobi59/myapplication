package com.example.myapplication;

import static com.example.myapplication.register.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {

    BottomNavigationView nav;
    Button mdecoButton;
    Button mChangeButton;
    EditText mnewEmail;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //instance de l'authentification
        mAuth = FirebaseAuth.getInstance();
        mdecoButton = findViewById(R.id.deconnexionButton);
        mChangeButton = findViewById(R.id.ChangeButton);
        mnewEmail = findViewById(R.id.newMail);
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
        mChangeButton.setOnClickListener(view ->{
            String newEmail = mnewEmail.getText().toString();
            updateEmail(newEmail);
        });
    }
    public void updateEmail(String mail) {
        // [START update_email]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(mail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                        Toast.makeText(settings.this, "Email changé !",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // [END update_email]
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
