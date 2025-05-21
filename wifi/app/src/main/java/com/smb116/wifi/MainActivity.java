package com.smb116.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    IntentFilter filterScan = new
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_WIFI_PERMISSION = 2;
    private int permissionCount = 0;
    private TextView textAffich;
    BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                // Recherche terminée récupération des points wifi
                wifiScanResults();
            }
        }
    };

    public void init() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
    }

    public void wifiScanResults() {
        @SuppressLint("MissingPermission")
        List<ScanResult> scanResults = wifiManager.getScanResults();
        String s = "";
        for(ScanResult scan:scanResults) {
            s +=  "SSID " + scan.SSID + "\n";
            s +=  "\tBSSID " + scan.BSSID + "\n";
            s += "\tFrequency: "+ String.valueOf(scan.frequency) + " Mhz\n";
            s += "\tLevel : "+ String.valueOf(scan.level) + " dBm\n\n";
            textAffich.setText(s);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionReceiver, filterScan);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectionReceiver);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission de localisation accordée", Toast.LENGTH_SHORT).show();
                    permissionCount++;
                } else {
                    Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_WIFI_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Wi-Fi accordée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Wi-Fi refusée", Toast.LENGTH_SHORT).show();
                    permissionCount++;
                }
                break;
        }
        permissionCheck();
    }

    public void permissionCheck() {
        if(permissionCount > 1)
            init();
    }

    public void demandePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }else {
            permissionCount++;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.ACCESS_WIFI_STATE},
                    REQUEST_WIFI_PERMISSION);
        }else {
            permissionCount++;
        }
        permissionCheck();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textAffich = findViewById(R.id.textAffich);
        demandePermission();
        permissionCheck();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}