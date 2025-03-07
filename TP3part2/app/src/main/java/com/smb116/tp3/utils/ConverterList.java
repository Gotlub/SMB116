package com.smb116.tp3.utils;

import android.content.Context;

import com.smb116.tp3.model.Borne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConverterList  {


    private Context context;
    private List<Borne> bornesList;

    public List<Borne> getBornesList() {
        return bornesList;
    }


    // Constructor
    public ConverterList(Context context, int qtt) {
        this.context = context;
        this.bornesList = new ArrayList<>();
        try {
            convert(getRawJSON(), qtt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void convert(String raw, int qtt) throws JSONException {
        JSONArray jsonArray = new JSONArray(raw);
        for(int i = 0; i < jsonArray.length() && i < qtt; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Borne borne = new Borne();
            borne.setId(obj.getInt("id"));
            borne.setNom(obj.getString("Nom de la borne"));
            borne.setAdresse(obj.getString("Adresse"));
            borne.setVille(obj.getString("Ville"));
            borne.setGps(obj.getString("GPS"));
            borne.setPuissance(obj.getString("Puissance"));
            borne.setStatut(obj.getInt("Statut de la borne"));
            borne.setPrix(obj.getString("Prix d'utilisation"));
            bornesList.add(borne);
        }
    }


    private String getRawJSON()  {
        String json = null;
        try {
            int resId = context.getResources().getIdentifier("bornes", "raw", context.getPackageName());
            InputStream is = context.getResources().openRawResource(resId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
