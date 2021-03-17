package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.clock.helper.Constants;

public class LabelActivity extends AppCompatActivity {
    Button completeButton;
    EditText labelEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        completeButton = findViewById(R.id.completeButton);
        labelEdit = findViewById(R.id.labelEdit);

        Intent intent = getIntent();
        labelEdit.setText(intent.getStringExtra(Constants.KEY_LABEL));

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = labelEdit.getText().toString().trim();

                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_LABEL, label.isEmpty() ? "알람" : label);
                setResult(Constants.RC_SUCCESS, intent);
                finish();
            }
        });
    }
}