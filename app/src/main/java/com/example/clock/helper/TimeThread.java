package com.example.clock.helper;

import android.content.Context;
import android.util.Log;

import com.example.clock.MainActivity;
import com.example.clock.adapter.AlarmAdepter;

public class TimeThread extends Thread {
    private Context context;
    private AlarmAdepter alarmAdepter;
    private StopwatchPage stopwatchPage;
    private TimerPage timerPage;

    public TimeThread(Context context, AlarmAdepter alarmAdepter, StopwatchPage stopwatchPage, TimerPage timerPage) {
        this.context = context;
        this.alarmAdepter = alarmAdepter;
        this.stopwatchPage = stopwatchPage;
        this.timerPage = timerPage;
    }

    @Override
    public void run() {
        while (true) {
            // TODO MainActivity.handleAlarm()
            // - checkAlarm()
            // - alarmAdepter.setSwithc...(), updateStopwatch(), updateTimer()
            // - alarmAdepter.notifyDataSetChanged(); (UI 쓰레드)
            alarmAdepter.checkAlarm();

            ((MainActivity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alarmAdepter.notifyDataSetChanged();
                    stopwatchPage.updateStopwatch();
                    timerPage.updateTimer();
                }
            });

            try {
                Thread.sleep(500); // TODO 500 -> 100
            } catch (InterruptedException e) {
                Log.d("test", e.toString());
            }
        }
    }
}
