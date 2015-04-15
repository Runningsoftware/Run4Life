package com.example.tawfiq.run4life;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ReportActivity extends ActionBarActivity {

    protected TextView mDistanceTextView;
    protected TextView mAvgSpeedTextView;
    protected TextView mDurationTextView;
    protected TextView mCalorietextView;

    protected Spinner spinner;

    protected Button deleteButton;

    ArrayList<Run> AllRuns;

    DecimalFormat df = new DecimalFormat("####0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
        mAvgSpeedTextView = (TextView) findViewById(R.id.AvgSpeedTextView);
        mDurationTextView = (TextView) findViewById(R.id.TimeTextView);
        mCalorietextView = (TextView) findViewById(R.id.CaloriesTextView);

        spinner = (Spinner) findViewById(R.id.spinner);
        deleteButton = (Button) findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(mOnClickListenerDeleteButton);

        loadSpinner();
        spinner.setOnItemSelectedListener(mOnItemSelectedListener);

    }

    protected  void loadSpinner()
    {

        AllRuns = MyService.loadAllRuns();
        ArrayList<String> AllRunsID = new ArrayList<>();
        for(int i = 0 ; i < AllRuns.size() ; i++)
            AllRunsID.add(AllRuns.get(i).getDateID());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, AllRunsID);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        if(AllRuns.size() == 0)
        {
            mDistanceTextView.setText((df.format(0) + " KM"));
            mAvgSpeedTextView.setText((df.format(0)) + " KM/Hr");
            mDurationTextView.setText("00" + ":" + "00" + ":" + "00");
            mCalorietextView.setText(df.format(0));
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.GONE);
        }
        else
        {
            deleteButton.setEnabled(true);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    private Spinner.OnItemSelectedListener mOnItemSelectedListener = new Spinner.OnItemSelectedListener() {




        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(AllRuns.size()>0)
            {
                long time = AllRuns.get(position).getDuration();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";

                mDistanceTextView.setText((df.format(AllRuns.get(position).getDistance()) + " KM"));
                mAvgSpeedTextView.setText((df.format(AllRuns.get(position).getAvgSpeed())) + " KM/Hr");
                mDurationTextView.setText(hh + ":" + mm + ":" + ss);
                mCalorietextView.setText(df.format(AllRuns.get(position).getNetCalories()));
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Button.OnClickListener  mOnClickListenerDeleteButton= new Button.OnClickListener() {


        @Override
        public void onClick(View v) {

            if(spinner.getSelectedItem() != null)
                MyService.deleteRun(spinner.getSelectedItem().toString());
            loadSpinner();
        }



    };
}
