package com.example.clock.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clock.R;
import com.example.clock.adapter.LapAdepter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StopwatchPage {
    private Context context;
    LapAdepter lapAdepter;
    Button startButton;
    Button stopButton;
    Button lapButton;
    Button resetButton;
    TextView watch;
    ListView lapList;
    Calendar startTime;
    Calendar tempCalender = Calendar.getInstance();
    Calendar previousLap = Calendar.getInstance();
    int mode = Constants.STOPWATCH_MODE_READY;

    public StopwatchPage(Context context) {
        this.context = context;
    }

    public View getPage(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_stopwatch_page, parent, false);

        startButton = view.findViewById(R.id.startButton);
        stopButton = view.findViewById(R.id.stopButton);
        lapButton = view.findViewById(R.id.lapButton);
        resetButton = view.findViewById(R.id.resetButton);
        watch = view.findViewById(R.id.watch);
        lapList = view.findViewById(R.id.lapList);

        lapAdepter = new LapAdepter(context);
        lapList.setAdapter(lapAdepter);

        bindEvents();

        return view;
    }

    private void bindEvents() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == Constants.STOPWATCH_MODE_READY) {
                    startTime = Calendar.getInstance();
                }
                else if(mode == Constants.STOPWATCH_MODE_STOP) {
                    Calendar calendar = Calendar.getInstance();
                    startTime.setTimeInMillis(calendar.getTimeInMillis() - tempCalender.getTimeInMillis());
                }

                mode = Constants.STOPWATCH_MODE_START;

                showButtonOfStopWatch(mode);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mode = Constants.STOPWATCH_MODE_STOP;

                showButtonOfStopWatch(mode);
                tempCalender.setTimeInMillis(calendar.getTimeInMillis() - startTime.getTimeInMillis());
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tempCalendar = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                tempCalendar.setTimeInMillis(calendar.getTimeInMillis() - startTime.getTimeInMillis());
                calendar.setTimeInMillis(tempCalendar.getTimeInMillis() - previousLap.getTimeInMillis());
                previousLap.setTimeInMillis(tempCalendar.getTimeInMillis());

                lapAdepter.add(calendar);
                lapAdepter.notifyDataSetChanged();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = Constants.STOPWATCH_MODE_READY;

                showButtonOfStopWatch(mode);
                watch.setText("00:00:00"); // TODO 함수로 빼거나 이미 있는 함수를 그냥 호출
                previousLap.setTimeInMillis(0);
                lapAdepter.clear();
                lapAdepter.notifyDataSetChanged();
            }
        });
    }

    public void updateStopwatch() {
        if(mode != Constants.STOPWATCH_MODE_START) return;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String stopwatch = simpleDateFormat.format(calendar.getTimeInMillis() - startTime.getTimeInMillis());

        watch.setText(stopwatch);
    }

    private void showButtonOfStopWatch(int stopwatchMode) {
        showStartButton(false);
        showStopButton(false);
        showLapButton(false);
        showResetButton(false);

        if(stopwatchMode == Constants.STOPWATCH_MODE_READY) showStartButton(true);
        else if(stopwatchMode == Constants.STOPWATCH_MODE_START) {
            showStopButton(true);
            showLapButton(true);
        }
        else if(stopwatchMode == Constants.STOPWATCH_MODE_STOP) {
            showStartButton(true);
            showResetButton(true);
        }
    }

    private void showStartButton(boolean visibility) {
        startButton.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showStopButton(boolean visibility) {
        stopButton.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showLapButton(boolean visibility) {
        lapButton.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showResetButton(boolean visibility) {
        resetButton.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
