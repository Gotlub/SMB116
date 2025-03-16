package com.smb116.tp4;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

public class Ticker extends Thread implements Parcelable {
    public static final String TIME_ACTION_TIC = "time_action_tic";
    public static final String COUNT_EXTRA = "count";

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    private long count;

    public Ticker(Context context, long count){
        this.context = context;
        this.count = count;
    }

    public static Creator<Ticker> CREATOR = new Creator<Ticker>() {
        @Override
        public Ticker createFromParcel(Parcel in) {
            return new Ticker(in);
        }

        @Override
        public Ticker[] newArray(int size) {
            return new Ticker[size];
        }
    };

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
            Log.d("logD ticker", String.valueOf(count));
            Log.d("logD ticker", String.valueOf(Ticker.COUNT_EXTRA) + " " + TIME_ACTION_TIC );
            Log.d("logD ticker", context.toString() );
            Log.d("logD ticker", intent.toString() );
            if(count<=10) {context.sendBroadcast(intent);}
            else { context.sendOrderedBroadcast(intent,null);}
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        //parcel.writeLong(count);
        parcel.writeValue(this);
    }

    private Ticker(Parcel in) {
        //count = in.readLong();
        MainActivity.ticker = ((Ticker)in.readValue(MainActivity.class.getClassLoader()));
    }

    public long getCount() {
        return count;
    }
}
