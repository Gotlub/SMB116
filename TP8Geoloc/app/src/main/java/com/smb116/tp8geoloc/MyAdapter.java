package com.smb116.tp8geoloc;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.smb116.tp8geoloc.R;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> satList;

    public MyAdapter(List<String> satList) {
        this.satList = satList;
    }

    public void setSatList(List<String> satList) {
        this.satList = satList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sat_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        String sat = satList.get(position);

        holder.satellite.setText(String.format(sat));
    }

    @Override
    public int getItemCount() {
        return satList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView satellite;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            satellite = itemView.findViewById(R.id.satellite);

        }
    }
}