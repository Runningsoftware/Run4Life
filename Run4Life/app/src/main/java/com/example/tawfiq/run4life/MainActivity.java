package com.example.tawfiq.run4life;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.tawfiq.run4life.MyService.Status;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity - Run4Life";

    protected static SQLiteDatabase db ;

    Status mStatus = Status.NOT_STARTED;

    protected Button StartPauseResumeButton = null;
    protected Button StopResetButton = null;
    protected Button SettingsButton = null;
    protected Button ReportViewButton = null;

    protected TextView mDistanceTextView;


    protected Chronometer mDurationChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("Run4Life", Context.MODE_PRIVATE, null);
        MyService.SetDataBase(db);
        setupUI();
        Profile temp = MyService.getUser();

       if(temp == null)
        {
            Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);

            MainActivity.this.startActivity(myIntent);
        }


    }



    protected void setupUI()
    {
        StartPauseResumeButton = (Button) findViewById(R.id.StartPauseResumeButton);
        StopResetButton = (Button) findViewById(R.id.StopResetButton);
        SettingsButton = (Button) findViewById(R.id.SettingsButton);
        ReportViewButton = (Button) findViewById(R.id.ViewReportButton);

        StartPauseResumeButton.setOnClickListener(mOnClickStartPauseResumeListener);
        StopResetButton.setOnClickListener(mOnClickStopResetButtonListener);
        SettingsButton.setOnClickListener(mOnClickSettingsButtonListener);
        ReportViewButton.setOnClickListener(mOnClickReportButtonListerner);
        mDistanceTextView = (TextView) findViewById(R.id.DistanceEditText);



        mDurationChrono  = (Chronometer) findViewById(R.id.chronometer);
        mDurationChrono.setOnChronometerTickListener(mOnChronometerTickListener);

        if(MyService.getUser()!= null)
          SettingsButton.setText(MyService.getUser().getName());

    }


    protected  void UpdateUI()
    {
        mStatus = MyService.getmStatus();

        if(mStatus == Status.NOT_STARTED)
        {
            StartPauseResumeButton.setText("Start");
            StopResetButton.setText("Stop");
            StopResetButton.setEnabled(false);
            mDurationChrono.stop();
           // stopTimer();
        }
        else if(mStatus == Status.RUNNING)
        {
            StartPauseResumeButton.setText("Pause");
            StopResetButton.setText("Stop");
            StopResetButton.setEnabled(true);
            mDurationChrono.start();
            if(timer == null)
              startTimer();
        }
        else if(mStatus == Status.PAUSE)
        {
            StartPauseResumeButton.setText("Resume");
            StopResetButton.setText("Stop");
            StopResetButton.setEnabled(true);
            mDurationChrono.start();
            if(timer == null)
                startTimer();
        }
        else if (mStatus == Status.FINISHED)
        {
            if(MyService.getTotalDistance()> 0)
                stopTimer();

            mDurationChrono.stop();
            StartPauseResumeButton.setText("Start");
            StopResetButton.setText("Stop");
            StopResetButton.setEnabled(false);
        }

    }

    /**
     * Handler to auto update UI
     */

    private  Timer timer =null;
    protected  void startTimer() {
        Log.i(TAG, "Timer start");
        if (timer == null)
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    mHandler.obtainMessage(1).sendToTarget();

                }
            }, 0, 1000);
        }
    }
    protected void stopTimer(){
      if(timer != null) {
          Log.i(TAG, "timer cancelled");
          timer.cancel();
          timer = null;
      }
    }
    public  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
           //do something
            mDistanceTextView.setText(String.valueOf(MyService.getTotalDistance()));
            UpdateUI();
            Log.i(TAG, "updating UI");
        }
    };
    @Override
    protected void onStart() {

        super.onStart();

    }

    protected void onResume(){
        super.onResume();
        UpdateUI();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }



    /**
     * button onclick listeners
     */
    private Button.OnClickListener mOnClickStartPauseResumeListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v)
        {

            Intent startIntent = new Intent(MainActivity.this, MyService.class);
            startIntent.setAction(Constants.ACTION.PLAY_ACTION);
            startService(startIntent);
            if(timer == null)
                 startTimer();

           // mDurationChrono.start();

            UpdateUI();

        }




    };

    private Button.OnClickListener mOnClickStopResetButtonListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {


             Intent stopIntent = new Intent(MainActivity.this, MyService.class);
             stopIntent.setAction(Constants.ACTION.STOP_ACTION);
             startService(stopIntent);

            stopTimer();
            //mDurationChrono.stop();
            UpdateUI();

        }

    };
    private Button.OnClickListener  mOnClickSettingsButtonListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {



            stopTimer();
            Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);

            MainActivity.this.startActivity(myIntent);



        }



    };

    private Button.OnClickListener  mOnClickReportButtonListerner = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {




            Intent myIntent = new Intent(MainActivity.this, ReportActivity.class);

            MainActivity.this.startActivity(myIntent);



        }



    };

    private Chronometer.OnChronometerTickListener mOnChronometerTickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            long time = MyService.getChronoTime();
            int h   = (int)(time /3600000);
            int m = (int)(time - h*3600000)/60000;
            int s= (int)(time - h*3600000- m*60000)/1000 ;
            String hh = h < 10 ? "0"+h: h+"";
            String mm = m < 10 ? "0"+m: m+"";
            String ss = s < 10 ? "0"+s: s+"";
            chronometer.setText(hh+":"+mm+":"+ss);

        }
    };

}
