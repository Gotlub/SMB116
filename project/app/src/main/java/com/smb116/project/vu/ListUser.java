package com.smb116.project.vu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.smb116.project.R;
import com.smb116.project.model.NContact;
import com.smb116.project.utils.AllUserAdapter;
import com.smb116.project.utils.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUser extends AppCompatActivity {

    private EditText searchBox;
    private ListView listView;
    private AllUserAdapter adapter;
    private List<NContact> utilisateurs;
    private int userId;

    private void loadAllUser() {
        String jsonEncoded = String.format("{\"id\":%d}",
                userId);
        RetrofitInstance.getApiInterface().getAllUser(jsonEncoded).enqueue(new Callback<List<NContact>>() {
            @Override
            public void onResponse(Call<List<NContact>> call, Response<List<NContact>> response) {
                Log.d("log d getContact demande", response.body().toString());
                if (response.isSuccessful()) {
                    udapteList(response.body());
                } else {
                    Log.d("log d API demande", "Erreur: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<NContact>> call, Throwable t) {
                Log.d("log d getContact fail demande", t.getLocalizedMessage());
                Log.d("log d getContact fail demande", call.toString());
            }
        });
    }

    public void udapteList(List<NContact> lesContactes) {
        utilisateurs.clear();
        utilisateurs.addAll(lesContactes);
        //adapter.setOriginalList(utilisateurs);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_user);
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", 0);
        searchBox = findViewById(R.id.searchBox);
        listView = findViewById(R.id.listView);
        utilisateurs = new ArrayList<>();
        adapter = new AllUserAdapter(this, utilisateurs);
        listView.setAdapter(adapter);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NContact user = adapter.getItem(position);
            if (user != null) {
                Toast.makeText(ListUser.this, "Sélectionné : " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        loadAllUser();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}