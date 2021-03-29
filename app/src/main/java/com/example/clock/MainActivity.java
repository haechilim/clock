package com.example.clock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clock.adapter.AlarmAdepter;
import com.example.clock.adapter.LapAdepter;
import com.example.clock.domain.Alarm;
import com.example.clock.helper.AlarmPage;
import com.example.clock.helper.Constants;
import com.example.clock.helper.StopwatchPage;
import com.example.clock.helper.TimeThread;
import com.example.clock.helper.TimerPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// TODO
// FrameLayout  !
// activity 패키지 (재시작)
// domain 패키지 (Alarm, Lap)  !
// helper 패키지 (TimeThread)  !
// bindEvents() !
// stopwatchMode -> enum
// Calendar -> LocalTime, LocalDate, LocalDateTime
public class MainActivity extends AppCompatActivity{
    AlarmPage alarmPage;
    StopwatchPage stopwatchPage;
    TimerPage timerPage;
    AlarmAdepter alarmAdepter;
    FrameLayout page;
    Button editButton;
    Button completeButton;
    Button addButton;
    TextView title;
    TextView textAlarm;
    TextView textStopwatch;
    TextView textTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page = findViewById(R.id.page);
        editButton = findViewById(R.id.editButton);
        completeButton = findViewById(R.id.completeButton);
        addButton = findViewById(R.id.addButton);
        title = findViewById(R.id.title);
        textAlarm = findViewById(R.id.textAlarm);
        textStopwatch = findViewById(R.id.textStopWatch);
        textTimer = findViewById(R.id.textTimer);
        alarmAdepter = new AlarmAdepter(this);
        alarmPage = new AlarmPage(this, alarmAdepter);
        stopwatchPage = new StopwatchPage(this);
        timerPage = new TimerPage(this);

        showPage(Constants.ALARM_PAGE);

        TimeThread timerThread = new TimeThread(this, alarmAdepter, stopwatchPage, timerPage);
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

        // TODO addAlarm()로 빼기
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

    private void showPage(int page1) {
        View alarmView = alarmPage.getPage(page);
        View stopwatchView = stopwatchPage.getPage(page);
        View timerView = timerPage.getPage(page);
        int white = Color.rgb(0xff, 0xff, 0xff);
        int orange = Color.rgb(0xff, 0xa5, 0x00);

        page.removeAllViews();
        textAlarm.setTextColor(white);
        textStopwatch.setTextColor(white);
        textTimer.setTextColor(white);

        if(page1 == Constants.ALARM_PAGE) {
            title.setText("알람");
            showActionBarButton(true);
            textAlarm.setTextColor(orange);
            page.addView(alarmView);
        }
        else if(page1 == Constants.STOPWATCH_PAGE) {
            title.setText("스톱워치");
            showActionBarButton(false);
            textStopwatch.setTextColor(orange);
            page.addView(stopwatchView);
        }
        else if(page1 == Constants.TIMER_PAGE) {
            title.setText("타이머");
            showActionBarButton(false);
            textTimer.setTextColor(orange);
            page.addView(timerView);
        }
    }

    private void showActionBarButton(boolean visibility) {
        editButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        addButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }




}