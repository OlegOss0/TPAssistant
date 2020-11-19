package com.pso.tpassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class MainReceiver extends BroadcastReceiver {
    public static final String TAG = MainReceiver.class.getSimpleName();
    private static final String TEST_PHONE_RECEIVER_CLASS = "com.pso.testphone.recervers.SystemBroadcastReceiver";
    private static final String TEST_PHONE_PACKAGE = "com.pso.testphone";
    public final static long TIME_DELAY_DEF = 1500;
    private static long delayTime = TIME_DELAY_DEF;

    private static final String ALARM_INTENT_FROM_AS = "alarmIntentFromAssistant";
    public static final String TIME_EXTRA = "time_extra";
    public static final String VER_EXTRA = "ver_extra";

    @Override
    public void onReceive(Context context, Intent intent) {
        String extraLong = intent.getStringExtra(TIME_EXTRA);
        if(extraLong != null && !extraLong.isEmpty()){
            delayTime = Long.parseLong(extraLong);
        }else {
            sendDirectNotification(context);
        }
        Log.e(TAG, "onReceive = Action = " + intent.getAction() + ", delay time = " + delayTime);

        createAlarm(context);
        startService(context, delayTime);
    }

    private void startService(Context context, long time){
        Log.e(TAG, "startService");
        Intent startServiceIntent = new Intent(context, MainService.class);
        startServiceIntent.putExtra(TIME_EXTRA, String.valueOf(time));
        ContextCompat.startForegroundService(context, startServiceIntent);
    }

    public static void startTestPhoneActivity(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.pso.testphone", "com.pso.testphone.MainActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void sentBroadcastAlarm(Context context){
        Intent intent = new Intent();
        intent.setAction(ALARM_INTENT_FROM_AS);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(TIME_EXTRA, delayTime);
        intent.putExtra(VER_EXTRA, BuildConfig.VERSION_NAME);
        context.sendBroadcast(intent);
    }

    public static void sendDirectNotification(Context context) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(TEST_PHONE_PACKAGE, TEST_PHONE_RECEIVER_CLASS));
        context.sendBroadcast(i);
    }

    public static void createAlarm(Context context) {
        Log.e(TAG, "Creating alarm ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 15);
        sendAlarm(context, calendar.getTimeInMillis());
    }

    private static void sendAlarm(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MainReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, time, pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC, time, pendingIntent);
        }
        Log.e(TAG, "Alarm created!");
    }
}
