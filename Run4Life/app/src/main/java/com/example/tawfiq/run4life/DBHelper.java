package com.example.tawfiq.run4life;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tawfiq on 4/11/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    protected static final String TAG = "DBHelper - Run4Life";

    public DBHelper(Context context) {
        super(context, "Run4Life", null,1);

        Log.i(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(TAG, "on create");
            db.execSQL("CREATE TABLE IF NOT EXISTS RunSets(DateID VARCHAR,Distance VARCHAR,TargetDistance VARCHAR,  AvgSpeed VARCHAR, " +
                    "TargetAvgSpeed VARCHAR, Duration VARCHAR, NetCalories VARCHAR, Score VARCHAR);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Profile(Name VARCHAR, Age VARCHAR, Weight VARCHAR, Height VARCHAR, BMI VARCHAR, BMR VARCHAR, Gender VARCHAR");


        }
        catch(Exception e)
        {
            Log.i(TAG, "on create"+ e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
