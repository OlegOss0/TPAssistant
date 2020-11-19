package com.pso.tpassistant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.pso.tpassistant.MainReceiver.TIME_EXTRA;
import static com.pso.tpassistant.MainReceiver.sendDirectNotification;

public class MainService extends Service {
    private final String CHANNEL_ID = BuildConfig.APPLICATION_ID;
    Handler mHandler;
    Notification mNotification;
    long timeDelay = MainReceiver.TIME_DELAY_DEF;


    Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.e(MainReceiver.TAG, "Run...");
            createNotificationChannel();
            createNotification();
            doMainWork();
            Log.e(MainReceiver.TAG, "Stop...");
            stopSelf();
        }
    };

    private void doMainWork() {
        MainReceiver.createAlarm(getApplicationContext());
        MainReceiver.sentBroadcastAlarm(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(MainReceiver.TAG, "onCreate service");
        mHandler = new Handler();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mainNotification = new NotificationChannel(CHANNEL_ID,
                    "TPAssistant", NotificationManager.IMPORTANCE_MIN);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mainNotification);
            }
        }
    }

    private void createNotification() {
        mNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentTitle("test")
                .setVibrate(null)
                .setSound(null)
                .build();
        startForeground(1, mNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String extraLong = intent.getStringExtra(TIME_EXTRA);
        if(extraLong != null && !extraLong.isEmpty()){
            timeDelay = Long.parseLong(extraLong);
        }
        Log.e(MainReceiver.TAG, "onStartCommand , delay time =" + timeDelay);
        mHandler.removeCallbacks(r);
        mHandler.postDelayed(r, timeDelay);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(MainReceiver.TAG, "onDestroy service");
        sendDirectNotification(getApplicationContext());
        super.onDestroy();
    }

    @Override
    public void onTrimMemory(int level) {
        sendDirectNotification(getApplicationContext());
        Log.e(MainReceiver.TAG, "onDestroy service");
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        sendDirectNotification(getApplicationContext());
        super.onLowMemory();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
