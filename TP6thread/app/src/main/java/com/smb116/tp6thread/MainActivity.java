package com.smb116.tp6thread;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView affichage;
    private EditText ipEdit;
    private Button btnStart, btnStop, btnIp;
    private ProgressBar progressBar;
    private Thread t;
    private boolean encours = false;
    private String launchString = "Pour lancer l'acquisition ..., cliquez sur start.";
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String value = String.valueOf(msg.obj);
            if (value.equals("Erreur")) {
                stopAction();
                affichage.setText(String.format("Erreur de connexion"));
                return;
            }
            if(encours && !value.equals("Exception value")) {
                affichage.setText(String.format("Humidité : %s %%", value));
                progressBar.setProgress((int)Float.parseFloat(value) * 10);
            }
        }
    };

    private void init() {
        affichage = (TextView) findViewById(R.id.textView2);
        ipEdit = (EditText) findViewById(R.id.selfIP);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnIp = (Button) findViewById(R.id.btnSelfIP);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(1000);
        affichage.setText(launchString);
        btnStopListener();
        btnStartListener();
        btnIpListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("lastSelfIP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ipEdit.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("lastSelfIP", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("ip", "");
        Log.d("log onResume ", savedName);
        ipEdit.setText(savedName);
    }

    private void btnIpListener() {
        btnIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionStart(ipEdit.getText().toString());
            }
        });
    }

    private void btnStartListener() {
        Log.d("###THREAD###" , "thread");
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionStart(null);
            }
        });
    }

    private void btnStopListener() {
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAction();
            }
        });
    }

    private void actionStart(String url) {
        encours = true;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (encours) {
                    try {
                        HumiditySensorAbstract ds2438;
                        Thread.sleep(1000L);
                        if (url == null) {
                            ds2438 = new HTTPHumiditySensor();
                        } else {
                            ds2438 = new HTTPHumiditySensor(url);
                        }
                        String value = String.valueOf(ds2438.value());
                        Log.d("handleMessage", value);
                        Message msg = handler.obtainMessage(i, value);
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        if (i == 0) {
                            Message msg = handler.obtainMessage(i, "Erreur");
                            handler.sendMessage(msg);
                        }
                    }
                    i++;
                }
            }
        });
        t.start();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnIp.setEnabled(false);
    }


    private void stopAction() {
        encours = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnIp.setEnabled(true);
        affichage.setText(launchString);
        progressBar.setProgress(0);
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