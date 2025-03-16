package com.smb116.tp4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final boolean I = true;
    private boolean onRun = false;
    private Ticker ticker = null;
    private Button btnStart;
    private Button btnStop;
    private Button btnFinish;
    private TextView r1Texte;
    private TextView r2Texte;
    private TextView r3Texte;


    private static long count;
    // ...
    private class Ticker extends Thread implements Serializable { // ligne à commenter en q3
        // private class Ticker extends Thread implements Parcelable // ligne à décommenter en q3
        public static final String TIME_ACTION_TIC = "time_action_tic";
        public static final String COUNT_EXTRA = "count";
        private Context context;
        public Ticker(Context context){
            this.context = context;
        }
        public void startTicker(){
            this.start();
        }
        public void stopTicker(){
            this.interrupt();
        }
        public void run(){
            Intent intent = new Intent();
            intent.setAction(TIME_ACTION_TIC);
            while(!isInterrupted()){
                SystemClock.sleep(1000L);
                count++;
                intent.putExtra(Ticker.COUNT_EXTRA, count);
                context.sendBroadcast(intent);
                //if(count<=10) // à décommenter pour q2
                context.sendBroadcast(intent);
                Log.d("ticker", String.valueOf(count));
                //else // à décommenter pour q2
                //context.sendOrderedBroadcast(intent,null); // à décommenter pour q2
            }
        }
    }
    private Receiver r1,r2,r3;
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onResume(){
        super.onResume();
        if(I) Log.i(TAG,"onResume");
        setReceive();
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
        outState.putSerializable("ticker", ticker); // à commenter pour q3
        // outState.putParcelable("ticker",ticker); // demandé en q3
    }
    public void onRestoreInstanceState(Bundle outState){
        super.onRestoreInstanceState(outState);
        if(I)Log.i(TAG,"onRestoreInstanceState");
        ticker = (Ticker)outState.getSerializable("ticker"); // à commenter pour q3
        // ticker = (Ticker)outState.getParcelable("ticker"); // demandé en q3
        // suivie d'une mise à jour de l'IHM
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

    private void btnStartListener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onRun) {
                    if(ticker == null ) {
                        r1 = new Receiver(r1Texte, "r1");
                        r2 = new Receiver(r2Texte, "r2");
                        r3 = new Receiver(r3Texte, "r3");
                        count = 0;
                    }
                    ticker = new Ticker(getApplicationContext());
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