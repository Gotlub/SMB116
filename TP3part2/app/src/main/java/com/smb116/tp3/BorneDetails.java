package com.smb116.tp3;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.smb116.tp3.databinding.ActivityBorneDetailsBinding;

public class BorneDetails extends AppCompatActivity {

    private ActivityBorneDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBorneDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // creating an employee list
        // of type Employee class
        Borne borne = null;
        Intent intent = getIntent();
        if (intent != null) {
            borne = intent.getParcelableExtra("borne");
        }
        Log.d("borne id" , String.valueOf(borne.getId()));
        Log.d("borne nom" , borne.getNom());
        Log.d("borne adresse" , borne.getAdresse());
        Log.d("borne ville" , borne.getVille());
        Log.d("borne gps" , borne.getGps());
        Log.d("borne puissance" , borne.getPuissance());
        Log.d("borne status" , String.valueOf(borne.getStatut()));
        Log.d("borne prix" , borne.getPrix());

        // it the emplist is not null the it has some data and display it
        if (borne != null) {
            binding.idD.setText(String.valueOf(borne.getId()));
            binding.nomD.setText(borne.getNom());
            binding.adresseD.setText(borne.getAdresse());
            binding.villeD.setText(borne.getVille());
            binding.gpsD.setText(borne.getGps());
            binding.puissanceD.setText(borne.getPuissance());
            binding.statutD.setText(String.valueOf(borne.getStatut()));
            binding.prixD.setText(borne.getPrix());
        }
    }
}