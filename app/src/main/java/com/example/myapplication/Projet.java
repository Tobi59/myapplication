package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Projet implements Serializable {
    private String id;
    private String Nom;
    private String Description;
    private String DatedeDebut;
    private String DatedeFin;
    private List<String> Participants;
    private List<String> Taches;




    public Projet(String id, String Nom, String Description, String DatedeDebut, String DatedeFin, List<String> Participants, List<String> Taches) {
        this.id = id;
        this.Nom = Nom;
        this.DatedeDebut = DatedeDebut;
        this.Description = Description;
        this.DatedeFin = DatedeFin;
        this.Participants = Participants;
        this.Taches =Taches;
    }
    public Projet() {
        // Constructeur sans argument requis par Firebase
    }



    public String getDescription() {
        return Description;
    }

    public String getDatededebut() {
        return DatedeDebut;
    }

    public String getDatedefin() {
        return DatedeFin;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return Nom;
    }
    public List<String> getParticipants() {
        return Participants;
    }

    public List<String> getTaches() {
        return Taches;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setDatededebut(String DatedeDebut) {
        this.DatedeDebut = DatedeDebut;
    }

    public void setDatedefin(String DatedeFin) {
        this.DatedeFin = DatedeFin;
    }
    public void setParticipants(List<String> participants) {
        Participants = participants;
    }

    public void setTaches(List<String> taches) {
        Taches = taches;
    }
}


