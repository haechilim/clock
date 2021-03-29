package com.example.clock.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.clock.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimerPage {
    private Context context;
    LinearLayout timePickerOfTimerPage;
    TextView countDownPage;
    Button startButtonOfTimer;
    Button cancelButtonOfTimer;
    Button stopButtonOfTimer;
    Button restartButtonOfTimer;
    TextView hourOfTimer;
    TextView minuteOfTimer;
    TextView secondOfTimer;
    Calendar startTimeOfTimer;
    Calendar tempCalenderOfTimer = Calendar.getInstance();
    Calendar countDown = Calendar.getInstance();
    int timerMode = Constants.TIMER_MODE_READY;

    public TimerPage(Context context) {
        this.context = context;
    }

    public View getPage(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_timer_page, parent, false);

        timePickerOfTimerPage = view.findViewById(R.id.timePickerOfTimer);
        countDownPage = view.findViewById(R.id.countDown);
        startButtonOfTimer = view.findViewById(R.id.startButtonOfTimer);
        cancelButtonOfTimer = view.findViewById(R.id.cancelButtonOfTimer);
        stopButtonOfTimer = view.findViewById(R.id.stopButtonOfTimer);
        restartButtonOfTimer = view.findViewById(R.id.restartButtonOfTimer);
        hourOfTimer = view.findViewById(R.id.hour);
        minuteOfTimer = view.findViewById(R.id.minute);
        secondOfTimer = view.findViewById(R.id.second);

        bindEvents();

        return view;
    }

    private void bindEvents() {
        startButtonOfTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMode = Constants.TIMER_MODE_START;
                showButtonOfTimer(timerMode);
                setCountDown();
                showCountDownPage(true);
            }
        });

        cancelButtonOfTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMode = Constants.TIMER_MODE_READY;
                showButtonOfTimer(timerMode);
                showCountDownPage(false);
            }
        });

        stopButtonOfTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMode = Constants.TIMER_MODE_STOP;
                showButtonOfTimer(timerMode);
            }
        });

        restartButtonOfTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMode = Constants.TIMER_MODE_START;
                startTimeOfTimer = Calendar.getInstance();
                tempCalenderOfTimer.setTimeInMillis(countDown.getTimeInMillis());
                showButtonOfTimer(timerMode);
            }
        });
    }

    public void updateTimer() {
        if(timerMode != Constants.TIMER_MODE_START) return;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        long elapsedTime = calendar.getTimeInMillis() - startTimeOfTimer.getTimeInMillis();

        countDown.setTimeInMillis(tempCalenderOfTimer.getTimeInMillis() - elapsedTime);

        String timer = simpleDateFormat.format(countDown.getTimeInMillis());

        countDownPage.setText(timer);

        if(countDown.get(Calendar.HOUR_OF_DAY) == 0 &&
                countDown.get(Calendar.MINUTE) == 0 && countDown.get(Calendar.SECOND) == 0) {
            timerMode = Constants.TIMER_MODE_READY;
            showButtonOfTimer(timerMode);
            showCountDownPage(false);
        }
    }

    private void showButtonOfTimer(int timerMode) {
        showStartButtonOfTimer(false);
        showStopButtonOfTimer(false);
        showCancelButtonOfTimer(false);
        showRestartButtonOfTimer(false);

        if(timerMode == Constants.TIMER_MODE_READY) showStartButtonOfTimer(true);
        else if(timerMode == Constants.TIMER_MODE_START) {
            showStopButtonOfTimer(true);
            showCancelButtonOfTimer(true);
        }
        else if(timerMode == Constants.TIMER_MODE_STOP) {
            showRestartButtonOfTimer(true);
            showCancelButtonOfTimer(true);
        }
    }

    // TODO 타이머 시간입력시 validation(적합성) 체크
    // 자리수, 숫자범위
    private void setCountDown() {
        int hour = Integer.parseInt(hourOfTimer.getText().toString());
        int minute = Integer.parseInt(minuteOfTimer.getText().toString());
        int second = Integer.parseInt(secondOfTimer.getText().toString());

        if(hour >= 24) {
            hour = 23;
            minute = 59;
            second = 59;
        }
        else if(minute >= 60) {
            minute = 59;
            second = 59;
        }
        else if(second > 59) second = 60;

        countDown.set(Calendar.HOUR_OF_DAY, hour);
        countDown.set(Calendar.MINUTE, minute);
        countDown.set(Calendar.SECOND, second);

        startTimeOfTimer = Calendar.getInstance();
        tempCalenderOfTimer.setTimeInMillis(countDown.getTimeInMillis());
    }

    private void showCountDownPage(boolean isShowCountDownPage) {
        if (isShowCountDownPage) {
            timePickerOfTimerPage.setVisibility(View.GONE);
            countDownPage.setVisibility(View.VISIBLE);
        }
        else {
            countDownPage.setVisibility(View.GONE);
            timePickerOfTimerPage.setVisibility(View.VISIBLE);
        }
    }

    private void showStartButtonOfTimer(boolean visibility) {
        startButtonOfTimer.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showStopButtonOfTimer(boolean visibility) {
        stopButtonOfTimer.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showCancelButtonOfTimer(boolean visibility) {
        cancelButtonOfTimer.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showRestartButtonOfTimer(boolean visibility) {
        restartButtonOfTimer.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
