package com.example.tawfiq.run4life;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


public final class DBHandler   {

    protected static final String TAG = "DBHandler - Run4Life";

    private SQLiteDatabase db = null;


    public DBHandler(SQLiteDatabase database) {

        if(db == null)
        {
            db = database;
            create();
        }
        Log.i(TAG, "constructor");
    }



    public void create(){

        try {
            Log.i(TAG, "on create");
            db.execSQL("CREATE TABLE IF NOT EXISTS RunSets(DateID VARCHAR,Distance VARCHAR,TargetDistance VARCHAR,  AvgSpeed VARCHAR, " +
                    "TargetAvgSpeed VARCHAR, Duration VARCHAR, NetCalories VARCHAR, Score VARCHAR);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Profile(Name VARCHAR, Age VARCHAR, Weight VARCHAR, Height VARCHAR, BMI VARCHAR, BMR VARCHAR, Gender VARCHAR);");


        }
        catch(Exception e)
        {
            Log.i(TAG, "on create"+ e.toString());
        }
    }



    public void saveProfile(Profile mProfile) {
        try
        {

            if(loadProfile() != null)
            {  db.execSQL("DELETE FROM Profile"); Log.i(TAG, "delete profile");}

            db.execSQL("INSERT INTO Profile VALUES('" + mProfile.getName() + "','" + mProfile.getAge() +
                    "','" + mProfile.getWeight() + "','" + mProfile.getHeight() + "','" + mProfile.getBMI() + "','" + mProfile.getBMR() + "','" + mProfile.getGender() + "' );");
            Log.i(TAG, "save profile hi" + mProfile.getWeight());
        }
        catch (Exception e)
        {
            Log.i(TAG, "save profile "+e.toString());
        }
    }

    public Profile loadProfile()
    {

        Profile temp = null;
        try
        {

            Cursor cursor = db.rawQuery("SELECT * FROM Profile; ", null);

            if (cursor != null) {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        temp = new Profile();
                        temp.setName(cursor.getString(cursor.getColumnIndex("Name")));
                        temp.setAge(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Age"))));
                        temp.setWeight(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Weight"))));
                        temp.setHeight(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Height"))));
                        temp.setBMI(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BMI"))));
                        temp.setBMR(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BMR"))));
                        temp.setGender(cursor.getString(cursor.getColumnIndex("Gender")));


                    }
                    while (cursor.moveToNext());
                }
                Log.i(TAG, "loadprofile");
                cursor.close();
            }
        }
        catch(Exception e)
        {
            Log.i(TAG, "load profile " + e.toString());
        }

        return temp;
    }
	//Save new run
	public void saveRunSet(Run mRun )
	 {
         try
         {

             db.execSQL("INSERT INTO RunSets VALUES('" + mRun.getDateID() + "','" + mRun.getDistance() +
                     "','" + mRun.getTargetDistance() + "','" + mRun.getAvgSpeed() + "','" + mRun.getTargetAvgSpeed() + "','" + mRun.getDuration() + "','" + mRun.getNetCalories() + "','" + mRun.getScore() + "' );");
             Log.i(TAG, "save run set");
         }
         catch(Exception e)
         {
             Log.i(TAG, "save run "+e.toString());
         }
     }

	/**
	 *load all the runs from database
     * return a list of Run objects
	 * @return
	 */
	public ArrayList<Run> loadAllRuns()
	{
        Log.i(TAG, "load all runs");
        ArrayList<Run> RunsList = new ArrayList<>();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM RunSets ", null
            );

            if (cursor.moveToFirst()) {
                do {
                    Run temp = new Run();
                    temp.setDateID(cursor.getString(cursor.getColumnIndex("DateID")));
                    temp.setDistance(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Distance"))));
                    temp.setTargetDistance((Double.parseDouble((cursor.getString(cursor.getColumnIndex("TargetDistance"))))));
                    temp.setAvgSpeed(Double.parseDouble(cursor.getString(cursor.getColumnIndex("AvgSpeed"))));
                    temp.setTargetAvgSpeed(Double.parseDouble(cursor.getString(cursor.getColumnIndex("TargetAvgSpeed"))));
                    temp.setDuration(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Duration"))));
                    temp.setNetCalories(Double.parseDouble(cursor.getString(cursor.getColumnIndex("NetCalories"))));
                    temp.setScore(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Score"))));
                    RunsList.add(temp);

                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        catch(Exception e)
        {
            Log.i(TAG, "load all runs "+e.toString());
        }
        return RunsList ;
    }



}
