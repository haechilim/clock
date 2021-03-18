package com.example.clock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clock.Adapter.AlarmAdepter;
import com.example.clock.helper.Constants;

public class MainActivity extends AppCompatActivity {
    AlarmAdepter alarmAdepter;
    Button editButton;
    Button completeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.alarmList);
        editButton = findViewById(R.id.editButton);
        completeButton = findViewById(R.id.completeButton);

        alarmAdepter = new AlarmAdepter(this);

        listView.setAdapter(alarmAdepter);

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

        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_IS_NEW_ALARM, true);
                startAddAlarmActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Constants.RC_SUCCESS) {
            String alarmTime = data.getStringExtra(Constants.KEY_ALARM_TIME);
            String label = data.getStringExtra(Constants.KEY_LABEL);

            alarmAdepter.add(new Alarm(alarmTime, label, true));
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
}