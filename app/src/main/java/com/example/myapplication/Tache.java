package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Tache implements Serializable {
    private String id;
    private String Nom;
    private String Description;
    private String DatedeDebut;
    private String DatedeFin;
    private List<String> Participants;
    private String Status;



    public Tache(String id, String Nom, String Description, String DatedeDebut, String DatedeFin, List<String> Participants, String Status) {
        this.id = id;
        this.Nom = Nom;
        this.DatedeDebut = DatedeDebut;
        this.Description = Description;
        this.DatedeFin = DatedeFin;
        this.Participants = Participants;
        this.Status = Status;
    }
    public Tache() {
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
    public void setStatus(String status) {
        Status = status;
    }
    public String getStatus() {
        return Status;
    }

}


