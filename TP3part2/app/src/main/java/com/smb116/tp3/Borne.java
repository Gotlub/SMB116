package com.smb116.tp3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Borne implements Parcelable {
    private int id;
    private String nom;
    private String adresse;
    private String ville;
    private String gps;
    private String puissance;
    private int statut;
    private String prix;

    /*
    public Borne(int id, String nom, String adresse, String ville, String gps, String puissance, int statut, String prix) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.gps = gps;
        this.puissance = puissance;
        this.statut = statut;
        this.prix = prix;
    }*/

    public  Borne(){

    }
    protected Borne(Parcel in) {
        id = in.readInt();
        nom = in.readString();
        adresse = in.readString();
        ville = in.readString();
        gps = in.readString();
        puissance = in.readString();
        statut = in.readInt();
        prix = in.readString();
    }

    public static final Creator<Borne> CREATOR = new Creator<Borne>() {
        @Override
        public Borne createFromParcel(Parcel in) {
            return new Borne(in);
        }

        @Override
        public Borne[] newArray(int size) {
            return new Borne[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getPuissance() {
        return puissance;
    }

    public void setPuissance(String puissance) {
        this.puissance = puissance;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nom);
        parcel.writeString(adresse);
        parcel.writeString(ville);
        parcel.writeString(gps);
        parcel.writeString(puissance);
        parcel.writeInt(statut);
        parcel.writeString(prix);
    }
}
