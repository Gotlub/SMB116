package com.smb116.myapplication.vue;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smb116.myapplication.R;
import com.smb116.myapplication.modele.User;
import com.smb116.myapplication.modele.UserContentProvider;
import com.smb116.myapplication.outils.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnInit;

    private Button btnAjouter;

    private Button btnActualiser;

    private RecyclerView recyclerView;

    private MyAdapter adapter;

    private UserContentProvider provider = null;

    private static final int CODE_AJOUT = 101;

    public UserContentProvider getProvider() {
        if (provider == null) provider = new UserContentProvider();
        return provider;
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

    private void AjoutUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserContentProvider.NOM_COLONNE_NOM, user.getNom());
        values.put(UserContentProvider.NOM_COLONNE_PRENOM, user.getPrenom());
        values.put(UserContentProvider.NOM_COLONNE_COURRIEL, user.getCourriel());
        Uri d = getContentResolver().insert(UserContentProvider.CONTENT_URI, values);
    }
    private void btnInitListener() {
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log mainActivity", "btnInit");
                getContentResolver().delete(Uri.parse("content://com.smb116.myapplication.provider/users"), null, null);
                AjoutUser(new User("Ntumba Wa Ntumba", "Patient", "patient.ntumba-wa-ntumba@lecnam.net"));
                AjoutUser(new User("Lemoine", "Frédéric", "frederic.lemoine@lecnam.net"));
                AjoutUser(new User("Jean-Ferdinand", "Susini", "jean-ferdinand.susini@lecnam.net"));
            }
        });
    }
    private void btnActualiserListener() {
        btnActualiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> users = new ArrayList<User>();
                Cursor cursor = getContentResolver().query(Uri.parse("content://com.smb116.myapplication.provider/users"), null, null, null, null);
                if(cursor.moveToFirst()) do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(UserContentProvider.NOM_COLONNE_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(UserContentProvider.NOM_COLONNE_NOM));
                    String prenom = cursor.getString(cursor.getColumnIndexOrThrow(UserContentProvider.NOM_COLONNE_PRENOM));
                    String courriel = cursor.getString(cursor.getColumnIndexOrThrow(UserContentProvider.NOM_COLONNE_COURRIEL));
                    User user = new User(nom, prenom, courriel);
                    user.setId(id);
                    users.add(user);
                } while (cursor.moveToNext());
                adapter.setUserList(users);
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