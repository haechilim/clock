package com.example.clock.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.clock.R;
import com.example.clock.adapter.AlarmAdepter;

public class AlarmPage {
    private Context context;
    private AlarmAdepter alarmAdepter;
    private ListView alarmList;

    public AlarmPage(Context context, AlarmAdepter alarmAdepter) {
        this.context = context;
        this.alarmAdepter = alarmAdepter;
    }

    public View getPage(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_alarm_page, parent, false);

        alarmList = view.findViewById(R.id.alarmList);
        alarmList.setAdapter(alarmAdepter);

        return view;
    }
}
