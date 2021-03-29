package com.example.clock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.clock.helper.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {
    Button cancelButton;
    Button saveButton;
    TextView alarmTime;
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a h:mm");

        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        alarmTime = findViewById(R.id.alarmTime);
        label = findViewById(R.id.label);

        Intent data = getIntent();

        // TODO 시간값이 넘어오면 편집, 아니면 새로 추가
        boolean isNewAlarm = data.getBooleanExtra(Constants.KEY_IS_NEW_ALARM, false);

        if(isNewAlarm) {
            alarmTime.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
            label.setText("알람");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_ALARM_TIME, calendar.getTimeInMillis());
                intent.putExtra(Constants.KEY_LABEL, label.getText().toString());
                setResult(Constants.RC_SUCCESS, intent);
                finish();
            }
        });

        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        alarmTime.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false);
                timePickerDialog.show();
            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_IS_NEW_ALARM, true);
                intent.putExtra(Constants.KEY_LABEL, label.getText().toString());
                intent.setClass(AddAlarmActivity.this, LabelActivity.class);
                startActivityForResult(intent, Constants.RC_SUCCESS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Constants.RC_SUCCESS) {
            label.setText(data.getStringExtra(Constants.KEY_LABEL));
        }
    }
}