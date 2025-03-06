package com.smb116.tp3;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Borne> bornList;

    // Constructor
    public MyAdapter(List<Borne> bornList) {
        this.bornList = bornList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.borne_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Borne borne = bornList.get(position);

        holder.nomB.setText(borne.getNom());
        holder.adresseB.setText(borne.getAdresse());
        holder.villeB.setText(borne.getVille());
        holder.gpsB.setText(borne.getGps());
        holder.puissance.setText(borne.getPuissance());
        holder.statutB.setText(String.valueOf(borne.getStatut()));
        holder.prixB.setText(borne.getPrix());
    }

    @Override
    public int getItemCount() {
        return bornList.size();
    }

    // ViewHolder class
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomB, adresseB, villeB, gpsB, puissance, statutB,  prixB;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            nomB = itemView.findViewById(R.id.nomB);
            adresseB = itemView.findViewById(R.id.adresseB);
            villeB = itemView.findViewById(R.id.villeB);
            gpsB = itemView.findViewById(R.id.gpsB);
            puissance = itemView.findViewById(R.id.puissance);
            statutB = itemView.findViewById(R.id.statutB);
            prixB = itemView.findViewById(R.id.prixB);
        }
    }
}
