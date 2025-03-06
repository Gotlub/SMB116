package com.smb116.tp3;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Sample data
        List<Borne> borneList = new ArrayList<>();

        borneList.add(new Borne(01, "Avenue des Platanes",
                "Avenue des Platanes", "Mordelles", "-1.8353097695116958,48.07971757105983",
                "150Kw",50 , "Non spécifié" ));

        borneList.add(new Borne(02, "TotalEnergies - VESOUL\"",
                "Avenue des Platanes", "Vesoul", "-1.8353097695116958,48.07971757105983",
                "150Kw",50 , "Non spécifié" ));
        borneList.add(new Borne(03, "prout - plop\"",
                "kelkepart", "labas", "-1.8353097695116958,48.07971757105983",
                "150Kw",50 , "Non spécifié" ));

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        MyAdapter adapter = new MyAdapter(borneList);
        Log.d("plop", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
    }
}