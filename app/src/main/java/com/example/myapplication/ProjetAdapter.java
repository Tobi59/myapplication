package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProjetAdapter extends RecyclerView.Adapter<ViewHolderProjet> {
    Context context;
    List<Projet> listeDeProjets;
    private static final String TAG = "AddProjectActivity";
    Projet projetsel;

    public ProjetAdapter(Context context, List<Projet> listeDeProjets, OnItemClickListener listener) {
        this.context = context;
        this.listeDeProjets = listeDeProjets;
        this.listener = listener;
    }
    public  ProjetAdapter(){

    }

    public interface OnProjetLoadedListener {
        void onProjetLoaded(String nomDeProjet);
    }
    public interface OnItemClickListener {
        void onItemClick(Projet projet);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void getNomDeProjet(String projetId, OnProjetLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Projets").document(projetId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nomDeProjet = documentSnapshot.getString("Nom");
                    listener.onProjetLoaded(nomDeProjet);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolderProjet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderProjet(LayoutInflater.from(context).inflate(R.layout.activity_projet_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProjet holder, int position) {
        Projet projet = listeDeProjets.get(position);
        projetsel = listeDeProjets.get(position);
        getNomDeProjet(projet.getId(), new OnProjetLoadedListener() {
            @Override
            public void onProjetLoaded(String nomDeProjet) {
                holder.NomProjet.setText(nomDeProjet);
                holder.Voirplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(projet);
                        }
                    }
                });
            }
        });
    }



    @Override
    public int getItemCount() {
        return listeDeProjets.size();
    }
}

