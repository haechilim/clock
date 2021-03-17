package com.example.clock.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.clock.Alarm;
import com.example.clock.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdepter extends BaseAdapter {
    private Context context;
    private static List<Alarm> list = new ArrayList<>();

    public AlarmAdepter(Context context) {
        this.context = context;
    }

    public void add(Alarm alarm) {
        list.add(alarm);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alarm alarm = list.get(position);

        String meridiem = alarm.getTime().split(" ")[0];
        String time = alarm.getTime().split(" ")[1];

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_alarm_list_item, parent, false);

        ((TextView)view.findViewById(R.id.meridiemText)).setText(meridiem);
        ((TextView)view.findViewById(R.id.timeText)).setText(time);
        ((TextView)view.findViewById(R.id.labelText)).setText(alarm.getLabel());

        view.findViewById(R.id.alarmSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
