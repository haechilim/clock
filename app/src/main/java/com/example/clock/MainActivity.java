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
import com.example.clock.helper.Constants;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    AlarmAdepter alarmAdepter;
    Button editButton;
    Button completeButton;
    Button addButton;
    TextView title;
    ListView alarmListView;
    LinearLayout stopwatchPage;
    TextView textAlarm;
    TextView textStopwatch;
    TextView textTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmListView = findViewById(R.id.alarmList);
        stopwatchPage = findViewById(R.id.stopwatchPage);
        editButton = findViewById(R.id.editButton);
        completeButton = findViewById(R.id.completeButton);
        addButton = findViewById(R.id.addButton);
        title = findViewById(R.id.title);
        textAlarm = findViewById(R.id.textAlarm);
        textStopwatch = findViewById(R.id.textStopWatch);
        textTimer = findViewById(R.id.textTimer);

        alarmAdepter = new AlarmAdepter(this);

        alarmListView.setAdapter(alarmAdepter);

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

    private void showActionBarButton(boolean visibility) {
        editButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        addButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}