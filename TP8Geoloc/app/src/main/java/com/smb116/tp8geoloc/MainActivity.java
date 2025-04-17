package com.smb116.tp8geoloc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssAntennaInfo;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.provider.ProviderProperties;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private TextView reseau, posDetail, listeSatellite, address;
    private LocationManager locationManager = null;
    ProviderProperties providerProperties ;

    private static final int GPS_CODE = 100;

    public GnssStatus.Callback mGnssStatusCallback = new GnssStatus.Callback() {

        @Override
        public void onFirstFix(int ttffMillis){
            super.onFirstFix(ttffMillis);
            Log.d("log d onFirstFix" , String.valueOf(ttffMillis));
            reseau.setText("GPS OK \n" +
                    "Time required to receive the fist fix: " + ttffMillis + "ms");
        }
        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            Log.d("log d 5" , String.valueOf(status.getSatelliteCount()));
            String satellites = "";
            for(int k = 0; k < status.getSatelliteCount() && k < 7; k++) {
                satellites += "Satellite " + status.getSvid(k) + "\n" +
                        "Az:" + status.getAzimuthDegrees(k) + "°,El" + status.getElevationDegrees(k) + "°, Snr:" + status.getBasebandCn0DbHz(k) +
                        "\nAlmanac: " + status.hasAlmanacData(k) +", Ephemeris: " + status.hasEphemerisData(k) +", UsedInFix: " + status.usedInFix(k) +"\n";

            }
            listeSatellite.setText(satellites);
        }

        @Override
        public void onStarted() {
            super.onStarted();
            Log.d("log d 6" , "coucou on start");
        }

        @Override
        public void onStopped() {
            super.onStopped();
            // GNSS a été arrêté
        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("log d locationListener", "Location update: " + location);
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
        listeSatellite = findViewById(R.id.listeSatellite);
        address = findViewById(R.id.address);

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
    private void locationLautcher() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        providerProperties = locationManager.getProviderProperties(LocationManager.GPS_PROVIDER); //pourquoi faire

        Log.d("log d 1" , String.valueOf(locationManager.getAllProviders()));
        Log.d("log d 2" , String.valueOf(providerProperties));
        LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        Log.d("log d 3" , String.valueOf(gpsProvider));
        Location localisation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        locationManager.registerGnssStatusCallback(mGnssStatusCallback);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30L, (float) 5, (LocationListener) null);
        if (localisation != null) {
            Log.d("log d 4" , localisation.toString());
            afficherLocation(localisation);
        }

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}