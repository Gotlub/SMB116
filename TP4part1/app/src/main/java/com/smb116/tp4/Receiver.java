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
    public Receiver(TextView r, String rTexte) {
        this.r = r;
        this.rTexte = rTexte;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String count = "count";
        //Toast.makeText(context, "Action: ", Toast.LENGTH_SHORT).show();
        if(Objects.equals(intent.getAction(), "time_action_tic")) {
            r.setText(String.format("%s : %s", rTexte, String.valueOf(intent.getLongExtra("count", 0))));
        }
    }
}