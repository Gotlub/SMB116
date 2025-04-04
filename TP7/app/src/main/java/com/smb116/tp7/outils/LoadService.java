package com.smb116.tp7.outils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.smb116.tp7.modele.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LoadService extends Service {

    private static final String TAG = LoadService.class.getSimpleName();
    private static HashMap<String, Station> hmap_stations = new HashMap<>();
    private static List<Station> stations;
    private InfoLoader infoLoader = null;
    private  Messenger messager;

    private static int runningNow = 0;

    public LoadService() {
    }

    public static HashMap<String, Station> getHmap_stations() {
        return hmap_stations;
    }

    public static List<Station> getStations() {
        return stations;
    }

    private class InfoLoader extends Thread {
        private String action;
        private InfoLoader(String action) {
            this.action = action;
        }

        public void run() {
            try {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                if(action.equals("loadStations")) {
                    loadStations(hmap_stations);
                    bundle.putString("load", "loadStations");
                } else if (action.equals("loadCapacity")) {
                    loadCapacity(hmap_stations);
                    bundle.putString("load", "loadCapacity");
                } else if (action.equals("convertList")) {
                    stations = new ArrayList<Station>(hmap_stations.values());
                    bundle.putString("load", "convertList");
                } else {
                    Message msg_2 = Message.obtain();
                    Bundle bundle_2 = new Bundle();
                    loadCapacity(hmap_stations);
                    Station station = hmap_stations.get(action);
                    bundle_2.putString("load", "abo");
                    bundle_2.putString("nom", station.getName());
                    bundle_2.putString("velo", "Vélo(s) disponible(s) :" + station.getNumBikesAvailable());
                    bundle_2.putString("place", "Place(s) disponible(s) :" + station.getNumDocksAvailable());
                    Log.d("log d thread", station.getName() + " " + station.getNumBikesAvailable() + " " + station.getNumDocksAvailable());
                    msg_2.setData(bundle_2);
                    messager.send(msg_2);
                    runningNow += 1;
                    Thread.sleep(10000);
                    bundle.putString("load", "reabo");
                    runningNow -= 1;
                    if(runningNow > 0)
                        return;

                }
                msg.setData(bundle);
                messager.send(msg);
            } catch (Exception e) {
                return;
            }
            Log.i(TAG,"Thread interrupted");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (infoLoader == null) {
            try {
                Bundle extras = intent.getExtras();
                String message = intent.getStringExtra("message");
                messager = (Messenger) extras.get("messager");
                Log.i(TAG, "onStartCommand");
                if(message.equals("loadStations")) {
                    infoLoader = new InfoLoader("loadStations");
                    infoLoader.start();
                    infoLoader.join();
                    infoLoader = new InfoLoader("loadCapacity");
                    infoLoader.start();
                    infoLoader.join();
                    infoLoader = new InfoLoader("convertList");
                    infoLoader.start();
                    infoLoader.join();
                    infoLoader = null;
                }else {
                    Log.d("log d onStartCommand", "demande abo : " + message);
                    infoLoader = new InfoLoader(message);
                    infoLoader.start();
                    infoLoader = null;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        if (infoLoader != null) {
            infoLoader.interrupt();
            infoLoader = null;
        }
    }

    public boolean loadStations(HashMap<String, Station> hmap_stations) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "https://velib-metropole-opendata.smovengo.cloud/opendata/Velib_Metropole/station_information.json";
        String jsonStr = sh.makeServiceCall(url);
        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr == null) {
            Log.e(TAG, "Couldn't get json from server.");
            return false;
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject j_data = jsonObj.getJSONObject("data");
            JSONArray stations = j_data.getJSONArray("stations");
            // looping through All stations
            for (int i = 0; i < stations.length(); i++) {
                JSONObject c = stations.getJSONObject(i);
                String station_id = c.getString("station_id");
                String name = c.getString("name");
                String lat = c.getString("lat");
                String lon = c.getString("lon");
                String capacity = c.getString("capacity");
                String stationCode = c.getString("stationCode");
                Log.i(TAG, "Json station item: " + station_id + " - "+name+" - "+lat+" - "+lon+" - "+capacity+" - "+stationCode);
                        Station s =new Station(station_id, name, lat, lon,capacity, stationCode);
                hmap_stations.put(station_id,s);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return true;
    }
    public boolean loadCapacity(HashMap<String, Station> hmap_stations) {
        HttpHandler sh = new HttpHandler();
// Making a request to url and getting response
        String url = "https://velib-metropole-opendata.smovengo.cloud/opendata/Velib_Metropole/station_status.json";
        String jsonStr = sh.makeServiceCall(url);
        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr == null) {
            Log.e(TAG, "Couldn't get json from server.");
            return false;
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject j_data = jsonObj.getJSONObject("data");
            JSONArray stations = j_data.getJSONArray("stations");
// looping through All stations
            for (int i = 0; i < stations.length(); i++) {
                JSONObject c = stations.getJSONObject(i);
                String station_id = c.getString("station_id");
                String numBikesAvailable = c.getString("numBikesAvailable");
                String numDocksAvailable = c.getString("numDocksAvailable");
                Station s =(Station)hmap_stations.get(station_id);
                if(s!=null) {
                    s.setNumBikesAvailable(numBikesAvailable);
                    s.setNumDocksAvailable(numDocksAvailable);
                    Log.i(TAG, "Json station item: " + station_id + " - " + numBikesAvailable + " - " + numDocksAvailable);
                } else {
                    Log.i(TAG, "Station Id not found : " + station_id);
                }
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return true;
    }
}