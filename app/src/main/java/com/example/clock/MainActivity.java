package com.example.clock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clock.Adapter.AlarmAdepter;
import com.example.clock.Adapter.LapAdepter;
import com.example.clock.helper.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {
    AlarmAdepter alarmAdepter;
    LapAdepter lapAdepter;
    Button editButton;
    Button completeButton;
    Button addButton;
    Button startButton;
    Button stopButton;
    Button lapButton;
    Button resetButton;
    TextView title;
    ListView alarmListView;
    ListView lapList;
    LinearLayout stopwatchPage;
    TextView textAlarm;
    TextView textStopwatch;
    TextView textTimer;
    Calendar startTimeOfTimer;
    Calendar tempCalender = Calendar.getInstance();
    Calendar previousLap = Calendar.getInstance();
    int timerMode = Constants.TIMER_MODE_READY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmListView = findViewById(R.id.alarmList);
        lapList = findViewById(R.id.lapList);
        stopwatchPage = findViewById(R.id.stopwatchPage);
        editButton = findViewById(R.id.editButton);
        completeButton = findViewById(R.id.completeButton);
        addButton = findViewById(R.id.addButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        lapButton = findViewById(R.id.lapButton);
        resetButton = findViewById(R.id.resetButton);
        title = findViewById(R.id.title);
        textAlarm = findViewById(R.id.textAlarm);
        textStopwatch = findViewById(R.id.textStopWatch);
        textTimer = findViewById(R.id.textTimer);
        previousLap.setTimeInMillis(0);

        alarmAdepter = new AlarmAdepter(this);
        lapAdepter = new LapAdepter(this);

        alarmListView.setAdapter(alarmAdepter);
        lapList.setAdapter(lapAdepter);

        showPage(Constants.ALARM_PAGE);

        TimeThread timerThread = new TimeThread(this, alarmAdepter);
        timerThread.start();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editModeUpdate(true);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editModeUpdate(false);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_IS_NEW_ALARM, true);
                startAddAlarmActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerMode == Constants.TIMER_MODE_READY) {
                    startTimeOfTimer = Calendar.getInstance();
                }
                else if(timerMode == Constants.TIMER_MODE_STOP) {
                    Calendar calendar = Calendar.getInstance();
                    startTimeOfTimer.setTimeInMillis(calendar.getTimeInMillis() - tempCalender.getTimeInMillis());
                }

                timerMode = Constants.TIMER_MODE_START;

                showButtonOfTimer(timerMode);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                timerMode = Constants.TIMER_MODE_STOP;

                showButtonOfTimer(timerMode);
                tempCalender.setTimeInMillis(calendar.getTimeInMillis() - startTimeOfTimer.getTimeInMillis());
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tempCalendar = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                tempCalendar.setTimeInMillis(calendar.getTimeInMillis() - startTimeOfTimer.getTimeInMillis());
                calendar.setTimeInMillis(tempCalendar.getTimeInMillis() - previousLap.getTimeInMillis());
                previousLap.setTimeInMillis(tempCalendar.getTimeInMillis());

                lapAdepter.add(calendar);
                lapAdepter.notifyDataSetChanged();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerMode = Constants.TIMER_MODE_READY;

                showButtonOfTimer(timerMode);
                ((TextView)findViewById(R.id.timer)).setText("00:00:00");
                previousLap.setTimeInMillis(0);
                lapAdepter.clear();
                lapAdepter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.alarmOfNavigationBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(Constants.ALARM_PAGE);
            }
        });

        findViewById(R.id.stopwatchOfNavigationBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(Constants.STOPWATCH_PAGE);
            }
        });

        findViewById(R.id.timerOfNavigationBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(Constants.TIMER_PAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        editModeUpdate(false);

        if(resultCode == Constants.RC_SUCCESS) {
            Calendar calendar = Calendar.getInstance();
            long alarmTime = data.getLongExtra(Constants.KEY_ALARM_TIME, 0);
            String label = data.getStringExtra(Constants.KEY_LABEL);

            calendar.setTimeInMillis(alarmTime);

            alarmAdepter.add(new Alarm(calendar, label, true));
            alarmAdepter.notifyDataSetChanged();
        }
    }

    public void startAddAlarmActivity(Intent intent) {
        intent.setClass(this, AddAlarmActivity.class);
        startActivityForResult(intent, Constants.RC_SUCCESS);
    }

    public void editModeUpdate(boolean isEditMode) {
        editButton.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        completeButton.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        alarmAdepter.editMode(isEditMode);
        alarmAdepter.notifyDataSetChanged();
    }

    public void updateStopWatch() {
        if(timerMode != Constants.TIMER_MODE_START) return;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String timer = simpleDateFormat.format(calendar.getTimeInMillis() - startTimeOfTimer.getTimeInMillis());

        ((TextView)findViewById(R.id.timer)).setText(timer);
    }

    private void showPage(int page) {
        int white = Color.rgb(0xff, 0xff, 0xff);
        int orange = Color.rgb(0xff, 0xa5, 0x00);

        alarmListView.setVisibility(View.GONE);
        stopwatchPage.setVisibility(View.GONE);

        textAlarm.setTextColor(white);
        textStopwatch.setTextColor(white);
        textTimer.setTextColor(white);

        if(page == Constants.ALARM_PAGE) {
            title.setText("알람");
            showActionBarButton(true);
            alarmListView.setVisibility(View.VISIBLE);
            textAlarm.setTextColor(orange);
        }
        else if(page == Constants.STOPWATCH_PAGE) {
            title.setText("스톱워치");
            showActionBarButton(false);
            stopwatchPage.setVisibility(View.VISIBLE);
            textStopwatch.setTextColor(orange);
        }
        else if(page == Constants.TIMER_PAGE) {
            title.setText("타이머");
            showActionBarButton(false);
            textTimer.setTextColor(orange);
        }
    }

    private void showButtonOfTimer(int timerMode) {
        showStartButton(false);
        showStopButton(false);
        showLapButton(false);
        showResetButton(false);

        if(timerMode == Constants.TIMER_MODE_READY) showStartButton(true);
        else if(timerMode == Constants.TIMER_MODE_START) {
            showStopButton(true);
            showLapButton(true);
        }
        else if(timerMode == Constants.TIMER_MODE_STOP) {
            showStartButton(true);
            showResetButton(true);
        }
    }

    private void showActionBarButton(boolean visibility) {
        editButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        addButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
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