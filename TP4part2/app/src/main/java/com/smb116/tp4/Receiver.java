package com.smb116.tp4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Receiver extends BroadcastReceiver {


    private TextView r;
    private String rTexte;

    private int endBrocast = -1;

    public void setEndBrocast(int endBrocast) {
        this.endBrocast = endBrocast;
    }

    public Receiver(TextView r, String rTexte) {
        this.r = r;
        this.rTexte = rTexte;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String count = "count";
        if(Objects.equals(intent.getAction(), "time_action_tic")) {
            long lCount = intent.getLongExtra("count", 0);
            r.setText(String.format("%s : %s", rTexte, String.valueOf(lCount)));
            if (isOrderedBroadcast() && endBrocast >= 0) {
                if(lCount > endBrocast) {
                    abortBroadcast();
                }

            }
        }
    }
}