package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mpasswordConfirm,mpasswordRegister, musernameRegister;
    Button mregisterButton;
    TextView mcreateText;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //récupère l'instance de Firebase pour accéder au projet
        mAuth = FirebaseAuth.getInstance();
        musernameRegister = findViewById(R.id.usernameRegister);
        mpasswordRegister = findViewById(R.id.passwordRegister);
        mpasswordConfirm = findViewById(R.id.passwordConfirm);
        mregisterButton = findViewById(R.id.registerButton);
        mcreateText = findViewById(R.id.createText);

        mcreateText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        mregisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String email = musernameRegister.getText().toString().trim();
                String password = mpasswordRegister.getText().toString().trim();
                String passwordConfirm = mpasswordConfirm.getText().toString().trim();
                //vérifie que l'email est bien remplie
                if(TextUtils.isEmpty(email)){
                    musernameRegister.setError("Username is required");
                    return;
                }
                //vérifie que le mot de passe est bien rempli
                if(TextUtils.isEmpty(password)){
                    mpasswordRegister.setError("Password is required");
                    return;
                }
                //vérifie que les 2 mots de passe concordent
                if(!passwordConfirm.equals(password)){
                    mpasswordConfirm.setError("Password is different");
                    return;
                }
                //appel de la fonction pour créer le compte dans firebase
                createAccount(email,password);
            }
        });
    }
    //class qui vérifie si l'utilisateur n'est pas déjà connecté
    @Override
    public void onStart() {
        super.onStart();
        // vérifie que l'utilisateur n'est pas connecté, mets à jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        System.out.println(currentUser);//debug
        if(currentUser!=null){
            System.out.println(currentUser);//debug
            startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
        }
    }
    //classe pour la création de compte
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)//méthode qui crée un user dans firebase
                .addOnCompleteListener(this, task -> {//lorsque c'est fait
                    if (task.isSuccessful()) {//si c'est réussi on met à jour l'ui avec les infos de l'utilisateur actuel
                        Log.d(TAG, "createUserWithEmail:success");
                        //rendez vous sur la page d'accueil
                        startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
                        FirebaseUser user = mAuth.getCurrentUser();//récupère les infos
                        updateUI(user);//Reload
                    } else {//si c'est un échec on prévient et on reload
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
    //class pour l'update de l'interface
    private void updateUI(FirebaseUser user) {
    }
}