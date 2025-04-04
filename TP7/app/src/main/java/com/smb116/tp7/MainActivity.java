package com.smb116.tp7;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smb116.tp7.modele.Station;
import com.smb116.tp7.outils.LoadService;
import com.smb116.tp7.outils.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean inLoad = false;
    private Intent intent;
    private HashMap<String, Station> hmap_stations = null;
    private static List<Station> stations = null;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Messenger messager;
    private Button btnCharger, btnAfficher;
    private TextView txtCharger, aboNom, aboVelo, aboPlace;
    private String idAbo;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle extras = msg.getData();
            if (extras != null) {
                String reponse = extras.getString("load");
                Log.d("log d", "Thread terminé " + reponse);
                if(reponse.equals("loadStations")) {
                    hmap_stations = LoadService.getHmap_stations();
                    Log.d("log d", "hmap_stations.size " + hmap_stations.size());
                    txtCharger.setText(hmap_stations.size() + " stations ont été chargées, capacité en attente");
                }
                if(reponse.equals("loadCapacity")) {
                    hmap_stations = LoadService.getHmap_stations();
                    txtCharger.setText(hmap_stations.size() + " stations ont été chargées");
                }
                if(reponse.equals("convertList")) {
                    txtCharger.setText(hmap_stations.size() + " stations ont été chargées et convertie");
                    stations = LoadService.getStations();
                    inLoad = false;
                }
                if(reponse.equals("abo")) {
                    Log.d("log d", "handleMessage Station actualisée");
                    Log.d("log d recepAbo", extras.getString("nom") + " " + extras.getString("velo") + " " + extras.getString("place"));
                    aboNom.setText(extras.getString("nom"));
                    aboVelo.setText(extras.getString("velo"));
                    aboPlace.setText(extras.getString("place"));
                }
                if(reponse.equals("reabo")) {
                    Log.d("log d", "handleMessage reabo");
                    reabo();
                }
            }
        }
    };

    private void init() {
        btnCharger = (Button) findViewById(R.id.btnCharger);
        txtCharger = (TextView) findViewById(R.id.txtStations);
        btnAfficher = (Button) findViewById(R.id.btnAfficher);
        aboNom = (TextView) findViewById(R.id.aboNom);
        aboVelo = (TextView) findViewById(R.id.aboVelo);
        aboPlace = (TextView) findViewById(R.id.aboPlace);
        btnCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inLoad) {
                    beginLoad();
                    inLoad = true;
                }
            }
        });

        btnAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stations != null) {
                    adapter.setUserList(stations);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Aucune stations chargée", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void beginLoad() {
        intent = new Intent(this, LoadService.class);
        intent.putExtra("message", "loadStations");
        messager =new Messenger(handler);
        intent.putExtra("messager", messager);
        startService(intent);
    }

    public void reabo() {
        intent.removeExtra("message");
        intent.putExtra("message", idAbo);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(new ArrayList<Station>());
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new MyAdapter.OnClickListener() {

            @Override
            public void onClick(int position, Station station) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(station.getName())
                        .setMessage( "Lat : " + station.getLat() +
                                "\nLon : " + station.getLon() +
                                "\nCapacitée " + station.getCapacity() +
                                "\nVélo(s) diponible(s) : " + station.getNumBikesAvailable() +
                                "\nDock(s) disponible(s) : " + station.getNumDocksAvailable() +
                                "\nVoulez-vous  vous abonner a cette station? ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                idAbo = station.getStation_id();
                                reabo();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }
}