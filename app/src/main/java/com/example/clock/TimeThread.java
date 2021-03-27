package com.example.clock;

import android.content.Context;
import android.util.Log;

import com.example.clock.Adapter.AlarmAdepter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeThread extends Thread {
    private Context context;
    private AlarmAdepter alarmAdepter;

    public TimeThread(Context context, AlarmAdepter alarmAdepter) {
        this.context = context;
        this.alarmAdepter = alarmAdepter;
    }

    @Override
    public void run() {
        while (true) {
            alarmAdepter.checkAlarm();

            ((MainActivity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alarmAdepter.notifyDataSetChanged();
                    ((MainActivity)context).updateStopWatch();
                }
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.d("test", e.toString());
            }
        }
    }
}
