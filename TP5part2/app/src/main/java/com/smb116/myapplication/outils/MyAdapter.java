package com.smb116.myapplication.outils;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.smb116.myapplication.R;
import com.smb116.myapplication.modele.User;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<User> userList;

    private View.OnClickListener onClickListener;

    public MyAdapter(List<User> UserList) {
        this.userList = UserList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nom.setText(user.getNom());
        holder.prenom.setText(user.getPrenom());
        holder.courriel.setText(user.getCourriel());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prenom, courriel;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            courriel = itemView.findViewById(R.id.courriel);
        }
    }
}
