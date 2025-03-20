package com.smb116.myapplication.modele;

public class User {
    private String id;

    private String nom;

    private String prenom;

    private String courriel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public User(String nom, String prenom, String courriel) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
    }
}
