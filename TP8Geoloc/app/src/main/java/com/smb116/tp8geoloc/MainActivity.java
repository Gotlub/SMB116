package com.smb116.tp8geoloc;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView reseau, posDetail, address;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> listSat;
    private LocationManager locationManager = null;
    private Geocoder geocoder;
    private static final int GPS_CODE = 100;

    public GnssStatus.Callback mGnssStatusCallback = new GnssStatus.Callback() {

        @Override
        public void onFirstFix(int ttffMillis){
            super.onFirstFix(ttffMillis);
            reseau.setText(String.format("GPS OK \nTime required to receive the fist fix: %dms", ttffMillis));
        }

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            Log.d("log d 5" , String.valueOf(status.getSatelliteCount()));
            listSat = new ArrayList<String>();
            for(int k = 0; k < status.getSatelliteCount(); k++) {        // && k < 7
                listSat.add("Satellite " + status.getSvid(k) + "\n" +
                        "Az:" + status.getAzimuthDegrees(k) + "°,El" + status.getElevationDegrees(k) + "°, Snr:" + status.getBasebandCn0DbHz(k) +
                        "\nAlmanac: " + status.hasAlmanacData(k) +", Ephemeris: " + status.hasEphemerisData(k) +", UsedInFix: " + status.usedInFix(k));

            }
            adapter.setSatList(listSat);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onStarted() {
            super.onStarted();
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("log d locationListener", "Location update: " + location);
            afficherLocation(location);
            afficherAdresse(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    private void init() {
        reseau = findViewById(R.id.reseau);
        posDetail = findViewById(R.id.posDetail);
        recyclerView = findViewById(R.id.recyclerview);
        address = findViewById(R.id.address);
        geocoder = new Geocoder(getApplicationContext(), Locale.FRANCE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},GPS_CODE);
            return;
        }
        locationLautcher();
    }

    private void afficherLocation(Location location) {
        posDetail.setText(String.format("Longitude: " + location.getLongitude() + "\n" +
                "Latitude: " + location.getLatitude() + "\n" +
                "Altitude: " + location.getAltitude() + "\n" +
                "Accuracy: " + location.getAccuracy() + "m\n"
                + "Speed:" + location.getSpeed() + "m/s\n" +
                "Bearing: " + location.getBearing() + "°\n" +
                "Time:" + location.getTime()) ) ;
    }
    private void afficherAdresse(Location location) {
        try {
            List<Address> adresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            StringBuilder addr = new StringBuilder();
            addr.append(adresses.get(0).getAddressLine(0)).append("\n");
            address.setText(addr);
            Log.d("log d addr", String.valueOf(addr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void locationLautcher() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 10, mLocationListener);
        locationManager.registerGnssStatusCallback(mGnssStatusCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null )
            locationManager.unregisterGnssStatusCallback(mGnssStatusCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GPS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationLautcher();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listSat = new ArrayList<String>();
        adapter = new MyAdapter(listSat);
        recyclerView.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}