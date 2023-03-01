package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderTache extends RecyclerView.ViewHolder {
    TextView NomTache,Voirplus;


    public ViewHolderTache(@NonNull View itemView) {
        super(itemView);
        NomTache = itemView.findViewById(R.id.Nomtache);
        Voirplus = itemView.findViewById(R.id.Voirplus);
    }

}

