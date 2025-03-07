package com.smb116.tp3.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.smb116.tp3.model.Borne;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownLoadAndParseJsonTask extends AsyncTask<String, Void, ArrayList<Borne>> {

    @Override
    protected ArrayList<Borne> doInBackground(String... params) {
        String urlString = params[0]; // L'URL du fichier JSON
        ArrayList<Borne> bornesList = new ArrayList<>();

        try {
            // Créer une URL et une connexion HTTP
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lire la réponse
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                response.append(line);
            }
            reader.close();

            // Parser le JSON
            // Parser le JSON
            JSONArray bornesArray = new JSONArray(response.toString());
            //JSONObject jsonObject = new JSONObject(response.toString());
            //JSONArray bornesArray = jsonObject.getJSONArray("bornes");
            Log.d("bornesArray", bornesArray.toString());
            // Insérer les 10 premiers éléments dans la liste
            for (int i = 0; i < Math.min(10, bornesArray.length()); i++) {
                Borne borne = new Borne();
                JSONObject stationObject = bornesArray.getJSONObject(i);
                int id = stationObject.getInt("id");
                borne.setId(stationObject.getInt("id"));
                borne.setNom(stationObject.getString("Nom de la borne"));
                borne.setAdresse(stationObject.getString("Adresse"));
                borne.setVille(stationObject.getString("Ville"));
                borne.setGps(stationObject.getString("GPS"));
                borne.setPuissance(stationObject.getString("Puissance"));
                borne.setStatut(stationObject.getInt("Statut de la borne"));
                borne.setPrix(stationObject.getString("Prix d'utilisation"));

                // Créer un objet BorneRechargeElectrique et l'ajouter à la liste

                bornesList.add(borne);
            }

        } catch (Exception e) {
            Log.e("DownloadAndParseJson", "Error downloading or parsing JSON", e);
        }

        return bornesList;
    }

    @Override
    protected void onPostExecute(ArrayList<Borne> result) {
        super.onPostExecute(result);
        // Afficher ou traiter les bornes récupérées
        if (result != null) {
            for (Borne borne : result) {
                //Log.d("DownloadAndParseJson", "Borne de recharge électrique: " + borne.getNom() + ", Adresse: " + borne.getAdresse()+ ", Ville: " + borne.getVille());
            }
        }
    }

}

