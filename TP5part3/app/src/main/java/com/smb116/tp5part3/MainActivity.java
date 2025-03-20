package com.smb116.tp5part3;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView nom, prenom, courriel;
    private Button afficherUser;

    private void init() {
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        courriel = findViewById(R.id.courriel);
        afficherUser = findViewById(R.id.btnAfficher);
        afficherUserListener();
    }

    private void afficherUserListener() {
        afficherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> users = new ArrayList<User>();
                try {
                    Cursor cursor = getContentResolver().query(Uri.parse("content://com.smb116.myapplication.provider/users"), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(cursor.getColumnIndexOrThrow(User.NOM_COLONNE_ID));
                            String nom = cursor.getString(cursor.getColumnIndexOrThrow(User.NOM_COLONNE_NOM));
                            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(User.NOM_COLONNE_PRENOM));
                            String courriel = cursor.getString(cursor.getColumnIndexOrThrow(User.NOM_COLONNE_COURRIEL));
                            User user = new User(nom, prenom, courriel);
                            user.setId(id);
                            users.add(user);
                        } while (cursor.moveToNext());
                        int sizeUser = users.size();
                        if (sizeUser > 0) {
                            final int random = new Random().nextInt(sizeUser);
                            User user = users.get(random);
                            nom.setText(user.getNom());
                            prenom.setText(user.getPrenom());
                            courriel.setText(user.getCourriel());
                        } else {
                            Toast.makeText(getApplicationContext(), "Pas de data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}