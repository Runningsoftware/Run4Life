package com.example.tawfiq.run4life;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public final class MyService extends Service implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    protected static final String TAG = "Service - Run4Life";

    static protected Profile mUser = null;

    protected static SQLiteDatabase db ;
    protected static DBHandler dbHandler = null;
    Notification notification = null;
    NotificationCompat.Builder builder = null;


    // Status of the current run: this enum is used to track the service's state
    public enum Status {
        NOT_STARTED, RUNNING, PAUSE, FINISHED;
    };

    static Status mStatus = Status.NOT_STARTED;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;



    /**
     * Provides the entry point to Google Play services.
     */
    protected static GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected static Location mCurrentLocation = null;

    protected static double distance_per_section = 0;
    /**
     * total distance
     */
    protected static double TotalDistance = 0 ;

    protected boolean isLocationUpdating = false;

    protected static long timeStartofRunSession = 0;
    protected static long savedRunningTime = 0;

    protected String mDateID;
    protected double mAvgSpeed =0;
    protected String FOREGROUND_TEXTVIEW = "Distance: " ;

    public MyService()
    {

    }
    public MyService(SQLiteDatabase database) {

        Log.i(TAG, "constructor service called");

    }


    public static void SetDataBase(SQLiteDatabase database)
    {
        if(db == null)
            db = database;
        Log.i(TAG, "set database called");
    }





    // Random number generator
    private static final Random mGenerator = new Random();

    //lisst for testing
    static List<Integer> numbers = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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

        Intent stopIntent = new Intent(this, MyService.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        PendingIntent pstopIntent = PendingIntent.getService(this, 0,
                stopIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.run4lifelogo);

        builder = new NotificationCompat.Builder(this)
                .setContentTitle("Run4Life")
                .setTicker("Run4Life")
                .setContentText(FOREGROUND_TEXTVIEW + (TotalDistance + distance_per_section)/1000)
                .setSmallIcon(R.drawable.run4lifelogo24x24)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_pause, "Pause",
                        pplayIntent)
                .addAction(android.R.drawable.ic_delete, "Stop",
                        pstopIntent);


        if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION))
        {


            if (mStatus == Status.NOT_STARTED || mStatus == Status.FINISHED)
            {
                Log.i(TAG, "Received Start Foreground Intent ");
                mStatus = Status.RUNNING;
                TotalDistance = 0;
                distance_per_section = 0;
                mCurrentLocation = null;
                //get the date and time and assign it as an ID to the new DataSet
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mDateID = sdf.format(new Date());

                if(!isLocationUpdating)
                {
                     //start requesting location update
                     startTimer();
                }

                builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Run4Life")
                        .setTicker("Run4Life")
                        .setContentText(FOREGROUND_TEXTVIEW + (TotalDistance + distance_per_section)/1000)
                        .setSmallIcon(R.drawable.run4lifelogo24x24)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .addAction(android.R.drawable.ic_media_pause, "Pause",
                                pplayIntent)
                        .addAction(android.R.drawable.ic_delete, "Stop",
                                pstopIntent);
                notification = builder.build();
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);

                //set time base
                timeStartofRunSession = SystemClock.elapsedRealtime();
                savedRunningTime = 0;

            }
            else if(mStatus == Status.RUNNING)
            {
                mStatus = Status.PAUSE;
                Log.i(TAG, "Clicked pause");
                builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Run4Life")
                        .setTicker("Run4Life")
                        .setContentText(FOREGROUND_TEXTVIEW + (TotalDistance + distance_per_section)/1000)
                        .setSmallIcon(R.drawable.run4lifelogo24x24)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .addAction(android.R.drawable.ic_media_play,"Resume",pplayIntent)
                        .addAction(android.R.drawable.ic_delete, "Stop",
                                pstopIntent);

                notification = builder.build();
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);
                stopLocationUpdates();

                //save time
                savedRunningTime = savedRunningTime + (SystemClock.elapsedRealtime() - timeStartofRunSession);

            }
            else if(mStatus == Status.PAUSE)
            {
                mStatus = Status.RUNNING;
                Log.i(TAG, "Clicked resume");
                builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Run4Life")
                        .setTicker("Run4Life")
                        .setContentText(FOREGROUND_TEXTVIEW + (TotalDistance + distance_per_section)/1000)
                        .setSmallIcon(R.drawable.run4lifelogo24x24)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .addAction(android.R.drawable.ic_media_pause,"Pause",pplayIntent)
                        .addAction(android.R.drawable.ic_delete, "Stop",
                                pstopIntent);
                notification = builder.build();
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);
                startLocationUpdates();

                //set time base
                timeStartofRunSession = SystemClock.elapsedRealtime();
            }

        }
        else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION))
        {
            if(mStatus != Status.FINISHED)
            {

                Log.i(TAG, "Clicked stop");

                stopLocationUpdates();
                stopForeground(true);

                mAvgSpeed = 3.6*((TotalDistance + distance_per_section))/(getChronoTime()/1000);

                //only save run in database if he actually moved his ass
            if(mAvgSpeed >= 0 && (TotalDistance + distance_per_section)/1000 >0.1 )
             {
                 Run temp = new Run(mDateID, (TotalDistance + distance_per_section) / 1000, 0, mAvgSpeed, 0, getChronoTime());
                 dbHandler.saveRunSet(temp);

             }
                mStatus = Status.FINISHED;
            }

        }
        return START_STICKY;
    }


    /**
     * Handler to auto update UI
     */

    private Timer timer = null;
    protected  void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                mHandler.obtainMessage(1).sendToTarget();

            }
        }, 0, 100);
    }

    protected void stopTimer()
    {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //do something

            Log.i(TAG, "updating start location update");
            if(mGoogleApiClient == null)
                buildGoogleApiClient();
            else if( !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
                mGoogleApiClient.connect();
            else if(mGoogleApiClient.isConnected()){

                startLocationUpdates();

                stopTimer();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "In onDestroy");
    }





    @Override
    public IBinder onBind(Intent intent)
    {
        // Used only in case of bound services.
        return null;
    }

    /** method for clients */
    public static int getRandomNumber()
    {
        numbers.add(mGenerator.nextInt(100));
        return numbers.size();
    }

    @Override
    public void onLocationChanged(Location location)
    {
       if(mCurrentLocation != null)
       {
           double tempdistance ;
           tempdistance = location.distanceTo(mCurrentLocation);

           if(tempdistance > distance_per_section)
           {
               distance_per_section = tempdistance;


               builder.setContentText(FOREGROUND_TEXTVIEW + (TotalDistance + distance_per_section)/1000);
               notification = builder.build();

               startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                       notification);
               if (distance_per_section > 200) {
                   TotalDistance = TotalDistance + distance_per_section;
                   distance_per_section = 0;

                   mCurrentLocation = location;

               }
           }

       }
        else
           mCurrentLocation = location;


    }



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks( this)
                .addOnConnectionFailedListener( this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        Log.i(TAG, "calling startLocationUpdate ");
        if(!isLocationUpdating)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            isLocationUpdating = true;
        }
    }

    protected void stopLocationUpdates(){
        Log.i(TAG, "calling stopLocationUpdate ");
        if(isLocationUpdating)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            isLocationUpdating = false;
        }
    }

    public static double getLocation()
    {


        if(mCurrentLocation != null)
            return mCurrentLocation.getLatitude();
        else
            return 100.0;
    }

    public static double getTotalDistance()
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.valueOf(df.format((TotalDistance + distance_per_section)/1000));
    }

    public static Status getmStatus(){
        Log.i(TAG, "calling getStatus");
        return  mStatus;}


    public static Profile getUser()
    {
        if(dbHandler == null)
            dbHandler = new DBHandler(db);

           // dbHandler.create();
            mUser = dbHandler.loadProfile();
            return mUser;

    }

    public static void saveProfile(Profile user)
    {
        if(dbHandler == null)
            dbHandler = new DBHandler(db);
       // dbHandler.create();
        dbHandler.saveProfile(user);

    }


    public static void deleteRun(String ID)
    {
        if(dbHandler == null)
            dbHandler = new DBHandler(db);
        dbHandler.deleteRun(ID);
    }
    public static long getChronoTime()
    {
       if(mStatus == Status.NOT_STARTED || mStatus == Status.FINISHED )
            return  0;
       else if(mStatus == Status.RUNNING)
           return  (SystemClock.elapsedRealtime() - timeStartofRunSession + savedRunningTime);
       else
           return savedRunningTime;

    }

    public static ArrayList<Run> loadAllRuns()
    {
        return dbHandler.loadAllRuns();
    }

}
