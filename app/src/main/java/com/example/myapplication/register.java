package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mpasswordConfirm,mpasswordRegister, musernameRegister;
    Button mregisterButton;
    TextView mcreateText;

    // refs à la DB
    DatabaseReference mDataBase = FirebaseDatabase.getInstance("https://myapplicationfirebase-7505e-default-rtdb.europe-west1.firebasedatabase.app").getReference("compte");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                final String username = musernameRegister.getText().toString().trim();
                String password = mpasswordRegister.getText().toString().trim();
                String passwordConfirm = mpasswordConfirm.getText().toString().trim();

                String userID = "6";
                if(TextUtils.isEmpty(username)){
                    musernameRegister.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mpasswordRegister.setError("Password is required");
                    return;
                }
                if(!passwordConfirm.equals(password)){
                    mpasswordConfirm.setError("Password is different");
                    return;
                }
                mDataBase.child("users").child(username).setValue("user"+userID);
                startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
            }
        });
    }
}
