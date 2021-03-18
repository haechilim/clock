package com.example.clock.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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

    public void editMode(boolean isEditMode) {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setEditMode(isEditMode);
        }
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

        updateSwitch(view, alarm.isSwitchOn());
        showDeleteButton(view, alarm.isEditMode());

        view.findViewById(R.id.alarmSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setSwitchOn(!alarm.isSwitchOn());

                updateSwitch(view, alarm.isSwitchOn());
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(alarm);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void updateSwitch(View view, boolean isAlarmSwitchOn) {
        LinearLayout alarmSwitch = view.findViewById(R.id.alarmSwitch);
        int color = isAlarmSwitchOn ? Color.rgb(0xff, 0xff, 0xff) : Color.rgb(0xae, 0xae, 0xae);

        alarmSwitch.setGravity(isAlarmSwitchOn ? Gravity.RIGHT : Gravity.LEFT);
        alarmSwitch.setBackground(isAlarmSwitchOn ? ContextCompat.getDrawable(context, R.drawable.layout_switch_frame_on)
                : ContextCompat.getDrawable(context, R.drawable.layout_switch_frame_off));

        ((TextView)view.findViewById(R.id.meridiemText)).setTextColor(color);
        ((TextView)view.findViewById(R.id.timeText)).setTextColor(color);
        ((TextView)view.findViewById(R.id.labelText)).setTextColor(color);
    }

    public void showDeleteButton(View view, boolean isEditMode) {
        view.findViewById(R.id.deleteButtonLayout).setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }
}
