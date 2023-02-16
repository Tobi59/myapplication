package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mpasswordConfirm,mpasswordRegister, musernameRegister;
    Button mregisterButton;
    TextView mcreateText;

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

                if(TextUtils.isEmpty(username)){
                    musernameRegister.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mpasswordRegister.setError("Password is required");
                    return;
                }

                if(passwordConfirm != password){
                    mpasswordConfirm.setError("Password is different");
                    return;
                }
                startActivity(new Intent(getApplicationContext(),welcomeActivity.class));
            }
        });
    }
}
