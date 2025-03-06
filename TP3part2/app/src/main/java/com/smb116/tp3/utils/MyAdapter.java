package com.smb116.tp3.utils;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smb116.tp3.Borne;
import com.smb116.tp3.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public void setBornList(List<Borne> bornList) {
        this.bornList = bornList;
    }

    private List<Borne> bornList;

    private OnClickListener onClickListener;

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
        holder.adresseB.setText(String.format("Adresse: %s", borne.getAdresse()));
        holder.villeB.setText(String.format("Ville: %s", borne.getVille()));
        holder.gpsB.setText(String.format("GPS: %s", borne.getGps()));
        holder.puissance.setText(String.format("Puissance: %s", borne.getPuissance()));
        holder.statutB.setText(String.format("Statut: %s", String.valueOf(borne.getStatut())));
        holder.prixB.setText(String.format("Prix: %s", borne.getPrix()));
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, borne);
            }
        });
    }

    // Setter for the click listener
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position, Borne borne);
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
