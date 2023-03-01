package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderProjet extends RecyclerView.ViewHolder {

    TextView NomProjet,Voirplus;


    public ViewHolderProjet(@NonNull View itemView) {
        super(itemView);
        NomProjet = itemView.findViewById(R.id.Nomprojet);
        Voirplus = itemView.findViewById(R.id.Voirplus);
    }
}
