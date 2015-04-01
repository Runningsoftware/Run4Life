package com.example.tawfiq.run4life;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyService extends Service {
    Notification notification = null;
    NotificationCompat.Builder builder = null;

    public MyService() {
        Log.i(TAG, "constructor service called");
    }




    protected static final String TAG = "Service - Run4Life";



    // Random number generator
    private static final Random mGenerator = new Random();

    //lisst for testing
    static List<Integer> numbers = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);


            Intent playIntent = new Intent(this, MyService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);

            Intent nextIntent = new Intent(this, MyService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                    nextIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.icontwitter24x24);

            builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Run4Life")
                    .setTicker("Run4Life")
                    .setContentText("Run Updates")
                    .setSmallIcon(R.drawable.icontwitter24x24)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_pause, "Pause Run",
                            pplayIntent)
                    .addAction(android.R.drawable.ic_delete, "Stop Run",
                            pnextIntent);
            notification = builder.build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);}
            else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                Log.i(TAG, "Clicked pause");
            builder.setContentText("run paused");
            notification = builder.build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                Log.i(TAG, "Clicked stop");
            builder.setContentText("run stopped");
            notification = builder.build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
            stopForeground(true);
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "In onDestroy");
    }





    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    /** method for clients */
    public static int getRandomNumber() {
        numbers.add(mGenerator.nextInt(100));
        return numbers.size();
    }
}
