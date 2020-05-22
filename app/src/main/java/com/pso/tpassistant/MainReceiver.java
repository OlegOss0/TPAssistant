package com.pso.tpassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class MainReceiver extends BroadcastReceiver {
    private static final String TAG = MainReceiver.class.getSimpleName();
    private final String TEST_PHONE_SERVICE_PACKAGE = "com.pso.testphone.services.MainService";
    private final String TEST_PHONE_ACTIVITY_PACKAGE = "com.pso.testphone.MainActivity";
    private static final String CHECK_EXTRA = "CHECK";
    private final int MINUTE_ALARM_CODE = 2;
    private Handler handler;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive = Action = " + intent.getAction());
        createAlarm(context);
        this.context = context;
        if(handler == null){
            handler = new Handler();
        }
        handler.post(h);
    }

    Runnable h = new Runnable() {
        @Override
        public void run() {
            if(context != null){
                handler.removeCallbacksAndMessages(h);
                startTestPhoneService(context);
                handler.postDelayed(h, 1000);
            }
        }
    };

    public static void startTestPhoneActivity(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.pso.testphone", "com.pso.testphone.MainActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void startTestPhoneService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.pso.testphone", "com.pso.testphone.services.MainService"));
        intent.getStringExtra(CHECK_EXTRA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextCompat.startForegroundService(context, intent);
    }

    private void createAlarm(Context context) {
        Log.e(TAG, "Creating alarm ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, MINUTE_ALARM_CODE);
        sendAlarm(context, calendar.getTimeInMillis());
    }

    private void sendAlarm(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MainReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, time, pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC, time, pendingIntent);
        }
        Log.e(TAG, "Alarm crated!");
    }
}
