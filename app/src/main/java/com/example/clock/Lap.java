package com.example.clock;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lap {
    private static int lapNumber = 1;
    private String lapName;
    private String time;

    public Lap(Calendar calendar, boolean isFirstLap) {
        if(isFirstLap) lapNumber = 1;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        lapName = "랩 " + lapNumber++;
        time = simpleDateFormat.format(calendar.getTimeInMillis());
    }

    public String getLapName() {
        return lapName;
    }

    public void setLapName(String lapName) {
        this.lapName = lapName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
