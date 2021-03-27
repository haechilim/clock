package com.example.clock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.clock.Lap;
import com.example.clock.R;
import com.example.clock.helper.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LapAdepter extends BaseAdapter {
    Context context;
    List<Lap> list = new ArrayList<>();

    public LapAdepter(Context context) {
        this.context = context;
    }

    public void add(Calendar calendar) {
        list.add(0, new Lap(calendar, list.isEmpty()));
    }

    public void clear() {
        list.clear();
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
        Lap lap = list.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_lap_list_item, parent, false);

        ((TextView)view.findViewById(R.id.lapName)).setText(lap.getLapName());
        ((TextView)view.findViewById(R.id.time)).setText(lap.getTime());

        return view;
    }
}
