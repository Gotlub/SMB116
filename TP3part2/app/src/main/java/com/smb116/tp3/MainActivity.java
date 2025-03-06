package com.smb116.tp3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smb116.tp3.utils.ConverterList;
import com.smb116.tp3.utils.MyAdapter;
import com.smb116.tp3.utils.DownLoadAndParseJsonTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCharger;
    private Button btnAjouter;
    private Button btnEffacer;

    private MyAdapter adapter;

    private RecyclerView recyclerView;

    private Button editAnnuler, editAjouter;
    private EditText editNom, editAdresse, editVille, editGPS, editPuissance, editStatut, editPrix;

    private void init() {
        btnCharger = findViewById(R.id.btnCharger);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnEffacer = findViewById(R.id.btnEffacer);
        butChargerListener();
        btnAjouterListener();
    }
    private void butChargerListener() {
        btnCharger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            public  void onClick(View v) {
                String url = "https://gitlab.roc.cnam.fr/algo-cloud/tp-rrpc/-/raw/main/movie/data/bornes.json";

                // Exécuter l'AsyncTask pour télécharger, parser et récupérer les 10 premières bornes de recharge electrique
                new DownLoadAndParseJsonTask() {
                    @Override
                    protected void onPostExecute(ArrayList<Borne> result) {
                        super.onPostExecute(result);
                        if (result != null) {
                            updateRecyclerView(result);
                        }
                    }
                }.execute(url);
            }
        });
    }

    private void btnAjouterListener() {
        Dialog dialog = new Dialog(MainActivity.this);

        btnAjouter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ajouter", "rentrer dans rajouter");
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                editAnnuler = dialog.findViewById(R.id.editAnnuler);
                editAjouter = dialog.findViewById(R.id.editAjouter);
                dialog.show();
            }
        });
    }

    public void updateRecyclerView(List<Borne>result) {
        adapter.setBornList(result);
        Toast.makeText(MainActivity.this, "Bornes chargées", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        recyclerView = findViewById(R.id.recyclerView);
        ConverterList convert = new ConverterList(getApplicationContext(), 5);
        // Sample data
        List<Borne> borneList = convert.getBornesList();

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        adapter = new MyAdapter(borneList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Borne borne) {
                Intent intent = new Intent(MainActivity.this, BorneDetails.class);
                intent.putExtra("borne", borne);
                startActivity(intent);
            }
        });
    }
}