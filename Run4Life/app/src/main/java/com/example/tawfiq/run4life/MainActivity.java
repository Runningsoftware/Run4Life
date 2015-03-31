package com.example.tawfiq.run4life;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity - Run4Life";


    protected Button StartPauseResumeButton = null;
    protected Button StopResetButton = null;
    protected Button SettingsButton = null;
    protected Button ReportViewButton = null;

    protected TextView mDistanceTextView;
    protected TextView mAvgSpeedTextView;
    protected TextView mDurationTextView;


   // MyService mService;
    ///boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
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

        mDistanceTextView = (TextView) findViewById(R.id.DistanceEditText);
        mAvgSpeedTextView = (TextView) findViewById(R.id.AvgSpeedEditText);
        mDurationTextView = (TextView) findViewById(R.id.DurationEditText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
      //  Intent intent = new Intent(this, MyService.class);


       // Intent startIntent = new Intent(MainActivity.this, MyService.class);
        //startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        //startService(startIntent);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {

            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = MyService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();

    }


    /**
     * button onclick listeners
     */
    private Button.OnClickListener mOnClickStartPauseResumeListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {

            Intent startIntent = new Intent(MainActivity.this, MyService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }



    };

    private Button.OnClickListener mOnClickStopResetButtonListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {


            Intent stopIntent = new Intent(MainActivity.this, MyService.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }

    };
    private Button.OnClickListener  mOnClickSettingsButtonListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v) {

            onButtonClick(v);


        }



    };



}
