package com.smb116.myapplication.vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.smb116.myapplication.R;
import com.smb116.myapplication.modele.User;
import com.smb116.myapplication.modele.UserHelper;

public class UserForm extends AppCompatActivity {

    private EditText nomUF, prenomUF, courrielUF;

    private Button annulerUF, ajouterUF;

    private void init() {
        nomUF = findViewById(R.id.nomUF);
        prenomUF = findViewById(R.id.prenomUF);
        courrielUF = findViewById(R.id.courrielUF);
        annulerUF = findViewById(R.id.btnAnnulerUF);
        ajouterUF = findViewById(R.id.btnAjouterUF);
        annulerUFListener();
        ajouterUFListener();
    }

    private void annulerUFListener() {
        annulerUF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ajouterUFListener() {
        ajouterUF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomUF.getText().toString();
                String prenom = prenomUF.getText().toString();
                String courriel = courrielUF.getText().toString();
                UserHelper helper = new UserHelper(getApplicationContext());
                helper.ajout( new User(nom, prenom, courriel));
                Intent intent = new Intent();
                intent.putExtra("nom", nom);
                intent.putExtra("prenom", prenom);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_form);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}