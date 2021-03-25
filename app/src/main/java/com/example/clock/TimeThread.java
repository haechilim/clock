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
        List<Calendar> alarmList;
        Calendar currentCalender;

        while (true) {
            alarmList = getCalendersOfAlarmTimeList();
            currentCalender = Calendar.getInstance();

            for(int i = 0; i < alarmList.size(); i++) {
                Calendar alarmTime = alarmList.get(i);

                if(currentCalender.get(Calendar.HOUR) == alarmTime.get(Calendar.HOUR) &&
                        currentCalender.get(Calendar.MINUTE) == alarmTime.get(Calendar.MINUTE) &&
                            currentCalender.get(Calendar.SECOND) == 0) {
                    alarmAdepter.setSwitchOn(i, false);

                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alarmAdepter.notifyDataSetChanged();
                        }
                    });
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.d("test", e.toString());
            }
        }
    }

    private List<Calendar> getCalendersOfAlarmTimeList() {
        List<Calendar> calendarList = new ArrayList<>();

        for(int i = 0; i < alarmAdepter.getSize(); i++) {
            calendarList.add(alarmAdepter.getAlarm(i).getTime());
        }

        return calendarList;
    }
}
