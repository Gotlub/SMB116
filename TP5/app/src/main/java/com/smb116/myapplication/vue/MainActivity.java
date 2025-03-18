package com.smb116.myapplication.vue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smb116.myapplication.R;
import com.smb116.myapplication.modele.User;
import com.smb116.myapplication.modele.UserHelper;
import com.smb116.myapplication.outils.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnInit;

    private Button btnAjouter;

    private Button btnActualiser;

    private RecyclerView recyclerView;

    private MyAdapter adapter;

    private UserHelper helper = null;

    private static final int CODE_AJOUT = 101;

    public UserHelper getHelper() {
        if (helper == null) helper = new UserHelper(MainActivity.this);
        return helper;
    }
    private void init() {
        btnInit = (Button) findViewById(R.id.btnInit);
        btnAjouter = (Button) findViewById(R.id.btnAjouter);
        btnActualiser = (Button) findViewById(R.id.btnActualiser);
        btnInitListener();
        btnAjouterListener();
        btnActualiserListener();
    }

    private void btnAjouterListener() {
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserForm.class );
                startActivityForResult(intent, CODE_AJOUT);
            }
        });
    }

    private void btnInitListener() {
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log mainActivity", "btnInit");
                helper = getHelper();
                helper.setUp();
                helper.ajout(new User("Ntumba Wa Ntumba", "Patient", "patient.ntumba-wa-ntumba@lecnam.net"));
                helper.ajout(new User("Lemoine", "Frédéric", "frederic.lemoine@lecnam.net"));
                helper.ajout(new User("Jean-Ferdinand", "Susini", "jean-ferdinand.susini@lecnam.net"));
            }
        });
    }
    private void btnActualiserListener() {
        btnActualiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setUserList(helper.recupUsers());
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(new ArrayList<User>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == CODE_AJOUT) {
            if (resultCode == RESULT_OK) {
                String nom = data.getStringExtra("nom");
                String prenom = data.getStringExtra("prenom");
                Toast.makeText(this, "Ajout de " + nom + " " + prenom, Toast.LENGTH_SHORT).show();
            }
        }
    }
}