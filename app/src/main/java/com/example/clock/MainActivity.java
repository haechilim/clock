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

public class MainActivity extends AppCompatActivity{
    AlarmAdepter alarmAdepter;
    LapAdepter lapAdepter;
    Button editButton;
    Button completeButton;
    Button addButton;
    Button startButton;
    Button stopButton;
    Button lapButton;
    Button resetButton;
    Button startButtonOfTimer;
    Button cancelButtonOfTimer;
    Button stopButtonOfTimer;
    Button restartButtonOfTimer;
    TextView title;
    ListView alarmListView;
    ListView lapList;
    LinearLayout stopwatchPage;
    LinearLayout timerPage;
    LinearLayout timePickerOfTimerPage;
    TextView countDownPage;
    TextView textAlarm;
    TextView textStopwatch;
    TextView textTimer;
    Calendar startTimeOfStopwatch;
    Calendar tempCalender = Calendar.getInstance();
    Calendar previousLap = Calendar.getInstance();
    Calendar startTimeOfTimer = Calendar.getInstance();
    Calendar countDown = Calendar.getInstance();
    Calendar tempCalenderOfTimer = Calendar.getInstance();
    TextView hourOfTimer;
    TextView minuteOfTimer;
    TextView secondOfTimer;
    int stopwatchMode = Constants.STOPWATCH_MODE_READY;
    int timerMode = Constants.TIMER_MODE_READY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmListView = findViewById(R.id.alarmList);
        lapList = findViewById(R.id.lapList);
        stopwatchPage = findViewById(R.id.stopwatchPage);
        timerPage = findViewById(R.id.timerPage);
        timePickerOfTimerPage = findViewById(R.id.timePickerOfTimer);
        countDownPage = findViewById(R.id.countDown);
        editButton = findViewById(R.id.editButton);
        completeButton = findViewById(R.id.completeButton);
        addButton = findViewById(R.id.addButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        lapButton = findViewById(R.id.lapButton);
        resetButton = findViewById(R.id.resetButton);
        startButtonOfTimer = findViewById(R.id.startButtonOfTimer);
        cancelButtonOfTimer = findViewById(R.id.cancelButtonOfTimer);
        stopButtonOfTimer = findViewById(R.id.stopButtonOfTimer);
        restartButtonOfTimer = findViewById(R.id.restartButtonOfTimer);
        hourOfTimer = findViewById(R.id.hour);
        minuteOfTimer = findViewById(R.id.minute);
        secondOfTimer = findViewById(R.id.second);
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
                if(stopwatchMode == Constants.STOPWATCH_MODE_READY) {
                    startTimeOfStopwatch = Calendar.getInstance();
                }
                else if(stopwatchMode == Constants.STOPWATCH_MODE_STOP) {
                    Calendar calendar = Calendar.getInstance();
                    startTimeOfStopwatch.setTimeInMillis(calendar.getTimeInMillis() - tempCalender.getTimeInMillis());
                }

                stopwatchMode = Constants.STOPWATCH_MODE_START;

                showButtonOfStopWatch(stopwatchMode);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                stopwatchMode = Constants.STOPWATCH_MODE_STOP;

                showButtonOfStopWatch(stopwatchMode);
                tempCalender.setTimeInMillis(calendar.getTimeInMillis() - startTimeOfStopwatch.getTimeInMillis());
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tempCalendar = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                tempCalendar.setTimeInMillis(calendar.getTimeInMillis() - startTimeOfStopwatch.getTimeInMillis());
                calendar.setTimeInMillis(tempCalendar.getTimeInMillis() - previousLap.getTimeInMillis());
                previousLap.setTimeInMillis(tempCalendar.getTimeInMillis());

                lapAdepter.add(calendar);
                lapAdepter.notifyDataSetChanged();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatchMode = Constants.STOPWATCH_MODE_READY;

                showButtonOfStopWatch(stopwatchMode);
                ((TextView)findViewById(R.id.timer)).setText("00:00:00");
                previousLap.setTimeInMillis(0);
                lapAdepter.clear();
                lapAdepter.notifyDataSetChanged();
            }
        });

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

    public void updateStopwatch() {
        if(stopwatchMode != Constants.STOPWATCH_MODE_START) return;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String stopwatch = simpleDateFormat.format(calendar.getTimeInMillis() - startTimeOfStopwatch.getTimeInMillis());

        ((TextView)findViewById(R.id.timer)).setText(stopwatch);
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

    private void showPage(int page) {
        int white = Color.rgb(0xff, 0xff, 0xff);
        int orange = Color.rgb(0xff, 0xa5, 0x00);

        alarmListView.setVisibility(View.GONE);
        stopwatchPage.setVisibility(View.GONE);
        timerPage.setVisibility(View.GONE);

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
            timerPage.setVisibility(View.VISIBLE);
            textTimer.setTextColor(orange);
        }
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