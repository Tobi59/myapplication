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

public class TacheAdapter extends RecyclerView.Adapter<ViewHolderTache> {
    Context context;
    List<Tache> listeDeTaches;
    private static final String TAG = "AddProjectActivity";
    Tache tachesel;

    public TacheAdapter(Context context, List<Tache> listeDeTaches, OnItemClickListener listener) {
        this.context = context;
        this.listeDeTaches = listeDeTaches;
        this.listener = listener;
    }
    public  TacheAdapter(){

    }

    public interface OnTacheLoadedListener {
        void onTacheLoaded(String nomDeTache);
    }
    public interface OnItemClickListener {
        void onItemClick(Tache tache);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void getNomDeTache(String tacheId, OnTacheLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Taches").document(tacheId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nomDeTache = documentSnapshot.getString("Nom");
                    listener.onTacheLoaded(nomDeTache);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolderTache onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTache(LayoutInflater.from(context).inflate(R.layout.activity_tache_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTache holder, int position) {
        Tache tache = listeDeTaches.get(position);
        tachesel = listeDeTaches.get(position);
        getNomDeTache(tache.getId(), new OnTacheLoadedListener() {
            @Override
            public void onTacheLoaded(String nomDeTache) {
                Log.d(TAG, "nom de tache :"+nomDeTache);
                holder.NomTache.setText(nomDeTache);
                holder.Voirplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(tache);
                        }
                    }
                });
            }
        });
    }





    @Override
    public int getItemCount() {
        return listeDeTaches.size();
    }
}

