package com.smb116.tp4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Receiver extends BroadcastReceiver  implements Parcelable {


    private TextView r;
    private String rTexte;

    private String memory;
    private int endBrocast = -1;

    protected Receiver(Parcel in) {
        memory = in.readString();
    }

    public static final Creator<Receiver> CREATOR = new Creator<Receiver>() {
        @Override
        public Receiver createFromParcel(Parcel in) {
            return new Receiver(in);
        }

        @Override
        public Receiver[] newArray(int size) {
            return new Receiver[size];
        }
    };

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
        Log.d("logD", "aLLloooooooooooooooo ? " + rTexte);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(r.getText().toString());
    }

    public String getMemory() {
        return memory;
    }
}