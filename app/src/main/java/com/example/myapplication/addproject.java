package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;


public class addproject extends AppCompatActivity {
    BottomNavigationView nav;
    //création de l'instance FireBase
    private FirebaseAuth mAuth;

    String[] friends = {"Alice", "Bob", "Charlie", "Dave", "Eve", "Frank"};
    private ListView mFriendsListView;
    private ArrayAdapter<String> mFriendsAdapter;
    private Button BoutonCreation;
    private TextView mSelectedCarateristiqueText;
    private Button mParticipants_button;
    private Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);

        //Recuperer le nom du projet
        TextInputLayout textProjectName = findViewById(R.id.Project_Name);
        TextInputEditText textProjectNameEditText = (TextInputEditText) textProjectName.getEditText();
        //Description
        TextInputLayout textDescription = findViewById(R.id.Description);
        TextInputEditText textDescriptionEditText = (TextInputEditText) textDescription.getEditText();
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
                        Date date = selectedDateDebut.getTime();
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
                        Date Date = selectedDateFin.getTime();
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        //Selection des participants
        mFriendsListView = findViewById(R.id.friends_listview);
        mFriendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, friends);
        mFriendsListView.setAdapter(mFriendsAdapter);
        mFriendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mSelectedCarateristiqueText = findViewById(R.id.selected_caracteristique_text);
        BoutonCreation = findViewById(R.id.button_creation);
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
        BoutonCreation.setOnClickListener(new View.OnClickListener() {
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
                mSelectedCarateristiqueText.setText("Nom du Projet : " + userProjectName +"\n"+ "Date de debut : "+  selectedDateDebut.getTime().toString()  + "\n"+ "Date de fin : "+ selectedDateFin.getTime().toString()  + "\n" +"Participants : " + selectedParticipantsString + "\n" +"Description" + userDescription);

            }
        });


        //Gestion des participants au projet
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
        // vérifie que l'utilisateur n'est pas connecté, mets à jour l'UI si besoin
        FirebaseUser currentUser = mAuth.getCurrentUser();//récupère les infos de l'utilisteur actuel
        if(currentUser==null){
            startActivity(new Intent(getApplicationContext(),register.class));
        }
    }

}