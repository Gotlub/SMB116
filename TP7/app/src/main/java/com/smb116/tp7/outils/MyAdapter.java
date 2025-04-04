package com.smb116.tp7.outils;

import android.app.Service;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.smb116.tp7.R;
import com.smb116.tp7.modele.Station;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Station> stationList;

   private OnClickListener onClickListener;

    public MyAdapter(List<Station> stationList) {
        this.stationList = stationList;
    }

    public void setUserList(List<Station> stationList) {
        this.stationList = stationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Station station = stationList.get(position);

        holder.idStation.setText(String.format("ID : %s", station.getStation_id()));
        holder.nomStation.setText(station.getName());
        holder.latStation.setText(String.format("lat : %s", station.getLat()));
        holder.lonStation.setText(String.format("lon : %s", station.getLon()));
        holder.capacityStation.setText(String.format("Capacitée : %s", station.getCapacity()));
        holder.bikeStation.setText(String.format("Vélo disponible : %s", station.getNumBikesAvailable()));
        holder.dockStation.setText(String.format("Doc disponible : %s", station.getNumDocksAvailable()));
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, station);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position, Station station);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idStation, nomStation, latStation, lonStation, capacityStation, bikeStation, dockStation;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            idStation = itemView.findViewById(R.id.idStation);
            nomStation = itemView.findViewById(R.id.nomStation);
            latStation = itemView.findViewById(R.id.latStation);
            lonStation = itemView.findViewById(R.id.lonStation);
            capacityStation = itemView.findViewById(R.id.capacityStation);
            bikeStation = itemView.findViewById(R.id.bikeStation);
            dockStation = itemView.findViewById(R.id.dockStation);
        }
    }
}