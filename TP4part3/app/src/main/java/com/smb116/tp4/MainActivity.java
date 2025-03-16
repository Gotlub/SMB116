package com.smb116.tp4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final boolean I = true;

    private static MainActivity instance;

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public  Ticker ticker = null;
    private boolean onRun = false;

    private Button btnStart;
    private Button btnStop;
    private Button btnFinish;
    private TextView r1Texte;
    private TextView r2Texte;
    private TextView r3Texte;

    private Receiver r1,r2,r3;
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onResume(){
        super.onResume();
        if(I) Log.i(TAG,"onResume");
        setReceive();
    }
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(r1);
        unregisterReceiver(r2);
        unregisterReceiver(r3);
    }
    private void setReceive() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Ticker.TIME_ACTION_TIC);
        filter.setPriority(100);
        registerReceiver(r1, filter);
        filter.setPriority(200);
        registerReceiver(r2, filter);
        filter.setPriority(300);
        registerReceiver(r3, filter);
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(I)Log.i(TAG,"onSaveInstanceState");
        outState.putParcelable("ticker",ticker); // demandé en q3
        outState.putParcelable("r1",r1);
        outState.putParcelable("r2",r2);
        outState.putParcelable("r3",r3);
    }
    public void onRestoreInstanceState(Bundle outState){
        super.onRestoreInstanceState(outState);
        if(I)Log.i(TAG,"onRestoreInstanceState");
        ticker = (Ticker)outState.getParcelable("ticker"); // demandé en q3
        r1Texte.setText(((Receiver)outState.getParcelable("r1")).getMemory());
        r2Texte.setText(((Receiver)outState.getParcelable("r2")).getMemory());
        r3Texte.setText(((Receiver)outState.getParcelable("r3")).getMemory());
        init();
        makeReceiver();
        // suivie d'une mise à jour de l'IHM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        instance = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init() {
        btnStart = (Button)findViewById(R.id.start);
        btnStop = (Button) findViewById(R.id.stop);
        btnFinish = (Button) findViewById(R.id.finish);
        r1Texte = (TextView) findViewById(R.id.r1);
        r2Texte = (TextView) findViewById(R.id.r2);
        r3Texte = (TextView) findViewById(R.id.r3);
        btnStartListener();
        btnStopListener();
        btnFinishListener();
    }

    private void makeReceiver() {
        r1 = new Receiver(r1Texte, "r1");
        r2 = new Receiver(r2Texte, "r2");
        r3 = new Receiver(r3Texte, "r3");
        r2.setEndBrocast(20);
    }
    private void btnStartListener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onRun) {
                    long count = 0;
                    if (ticker == null) {
                        makeReceiver();
                    } else {
                        count = ticker.getCount();
                    }
                    ticker = new Ticker(getApplicationContext(), count);
                    ticker.startTicker();
                    setReceive();
                    onRun = true;
                }
            }
        });
    }

    private void btnStopListener() {
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onRun) {
                    r1.setMemory(r1Texte.getText().toString());
                    r2.setMemory(r2Texte.getText().toString());
                    r3.setMemory(r3Texte.getText().toString());
                    ticker.stopTicker();
                    onRun = false;
                }
            }
        });
    }

    private void btnFinishListener() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
                System.exit(0);
            }
        });
    }
}