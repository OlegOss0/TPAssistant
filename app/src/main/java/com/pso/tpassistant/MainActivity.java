package com.pso.tpassistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStart, btnBind, btnUnbind, btnStop, btnStartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btn_start);
        btnBind = findViewById(R.id.btn_bind);
        btnUnbind = findViewById(R.id.btn_unbind);
        btnStop = findViewById(R.id.btn_stop);
        btnStartActivity = findViewById(R.id.btnStartActivity);
        btnStart.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStartActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                MainReceiver.startTestPhoneService(this);
                finish();
                break;
            case R.id.btnStartActivity:
                MainReceiver.startTestPhoneActivity(this);
                break;
            case R.id.btn_bind:
                break;
            case R.id.btn_unbind:
                break;
            case R.id.btn_stop:
                break;
        }
        ;
    }
}
