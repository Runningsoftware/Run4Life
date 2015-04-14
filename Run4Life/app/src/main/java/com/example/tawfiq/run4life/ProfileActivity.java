package com.example.tawfiq.run4life;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends ActionBarActivity {
    protected static final String TAG = "Profile-Run4Life";

    protected TextView mBMITextView;
    protected TextView mBMRTextView;
    protected TextView mBMIHeader;
    protected TextView mBMRHeader;
    protected EditText mNameEditText;
    protected EditText mWeightEditText;
    protected EditText mHeightEditText;
    protected EditText mAgeEditText;
    protected Switch GenderSwitch;

    protected Button Save;

    boolean isEdit = false;

    Profile User = null ;

    void displayToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_profile);
        setupUI();
        User = MyService.getUser();
        if(User == null)
            isEdit = true;
        else
        {
            isEdit = false;
            displayUser();

        }

        UpdateUI();
    }
    protected void displayUser()
    {
        mBMITextView.setText(Integer.toString(User.getBMI()));
        mBMRTextView.setText(Integer.toString(User.getBMR()));
        mNameEditText.setText(User.getName());
        mAgeEditText.setText(Integer.toString(User.getAge()));
        mHeightEditText.setText(Integer.toString(User.getHeight()));
        mWeightEditText.setText(Integer.toString(User.getWeight()));
        if(User.getGender().equals("M"))
            GenderSwitch.setChecked(false);
        else if (User.getGender().equals("F"))
            GenderSwitch.setChecked(true);
    }
    protected void setupUI()
    {
        Save = (Button) findViewById(R.id.saveButton);

        Save.setOnClickListener(mOnClickSaveButtonListener);


        mBMITextView = (TextView) findViewById(R.id.bmiValue);
        mBMRTextView = (TextView) findViewById(R.id.bmrValue);
        mBMIHeader = (TextView) findViewById(R.id.bmiHeader);
        mBMRHeader = (TextView) findViewById(R.id.bmrHeader);
        mNameEditText = (EditText) findViewById(R.id.NameEditText);
        mAgeEditText = (EditText) findViewById(R.id.AgeEditText);
        mWeightEditText = (EditText) findViewById(R.id.WeightEditText);
        mHeightEditText = (EditText) findViewById(R.id.HeightEditText);
        GenderSwitch = (Switch) findViewById(R.id.genderSwitch);
        GenderSwitch.setTextOff("M");
        GenderSwitch.setTextOn("F");

    }

    protected void UpdateUI()
    {
        if(isEdit)
        {
            mBMITextView.setVisibility(View.GONE);
            mBMRTextView.setVisibility(View.GONE);
            mBMIHeader.setVisibility(View.GONE);
            mBMRHeader.setVisibility(View.GONE);
            mNameEditText.setFocusableInTouchMode(true);
            mWeightEditText.setFocusableInTouchMode(true);
            mHeightEditText.setFocusableInTouchMode(true);
            mAgeEditText.setFocusableInTouchMode(true);
            GenderSwitch.setEnabled(true);
            Save.setEnabled(true);
            Save.setVisibility(View.VISIBLE);

        }
        else
        {
            mBMITextView.setVisibility(View.VISIBLE);
            mBMRTextView.setVisibility(View.VISIBLE);
            mBMIHeader.setVisibility(View.VISIBLE);
            mBMRHeader.setVisibility(View.VISIBLE);
            mNameEditText.setFocusable(false);
            mAgeEditText.setFocusable(false);
            mWeightEditText.setFocusable(false);
            mHeightEditText.setFocusable(false);
            GenderSwitch.setEnabled(false);
            Save.setEnabled(false);
            Save.setVisibility(View.GONE);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_settings:
                Log.i(TAG, "Clicked edit");
                isEdit = true;
                UpdateUI();
                return true;
            case android.R.id.home:
                if(!isEdit)
                    NavUtils.navigateUpFromSameTask(this);
                else
                    displayToast("Please insert your information and save.");
                return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private Button.OnClickListener mOnClickSaveButtonListener = new Button.OnClickListener() {


        @Override
        public void onClick(View v)
        {
            if(mAgeEditText.getText().toString().equals("") || mWeightEditText.getText().toString().equals("")||mHeightEditText.getText().toString().equals("")||mNameEditText.getText().toString().equals(""))
                displayToast("Insert all correct information!");
            else
            {
                //Profile temp = new Profile("Tawfiq", 21,70,178,"F");
                //MyService.saveProfile(temp);
                String genderTemp;
                if(GenderSwitch.isChecked())
                    genderTemp = "F";
                else
                    genderTemp = "M";
                Profile temp = new Profile(mNameEditText.getText().toString(),Integer.parseInt(mAgeEditText.getText().toString()),Integer.parseInt(mWeightEditText.getText().toString()),Integer.parseInt(mHeightEditText.getText().toString()),genderTemp);
                MyService.saveProfile(temp);
                User = temp;
                displayUser();
                isEdit = false;
                UpdateUI();
            }

// , 0)

        }




    };
}
