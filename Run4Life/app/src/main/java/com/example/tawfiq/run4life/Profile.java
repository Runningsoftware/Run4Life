package com.example.tawfiq.run4life;

import android.util.Log;

public class Profile{


    protected static final String TAG = "Profile - Run4Life";
	//  --------------------- private members

	
	private String mName;
	private int mAge;
	private int mWeight;
	private int mHeight;
	private int mBMI;
	private int mBMR;
	private String mGender;

	//  ----------------------------- constructor
	
	Profile(){
		
		setName("");
		setAge(0);
		setWeight(0);
		setHeight(0);
		setGender("");
	}

	
	Profile(String Name, int Age, int Weight, int Height, String Gender){
		
		setName(Name);
		setAge(Age);
		setWeight(Weight);
		setHeight(Height);
		setGender(Gender);
		setBMI();
		setBMR();
	}
	
	//  --------------------- getset
	
	public String getName() {
		return mName;
	}

	public void setName(String Name) {
		mName = Name;
	}
	
	public String getGender() {
		return mGender;
	}


	public void setGender(String Gender) {
		mGender = Gender;
	}


	public int getAge() {
		return mAge;
	}


	public void setAge(int Age) {
		mAge = Age;
	}

	public int getWeight() {
		return mWeight;
	}


	public void setWeight(int Weight) {
		mWeight = Weight;
	}


	public int getHeight() {
		return mHeight;
	}


	public void setHeight(int Height) {
		mHeight = Height;
	}


	public int getBMI() {
		return mBMI;
	}


	public void setBMI() {
		mBMI = calculateBMI();
	}

    public void setBMI(int BMI)
    {
        mBMI = BMI;
    }
	public int getBMR() {
		return mBMR;
	}


	public void setBMR() {
		mBMR = calculateBMR();
	}

    public void setBMR(int bmr){mBMR = bmr;}
	// --------------------- methods


	public int calculateBMI()
    {
        Double weight = (double)mWeight;
        Double height = (double)mHeight;
		Log.i(TAG,"BMI"+weight.toString()+" "+height.toString()+" "+(weight/(height/100) * (height/100)));
      	return (int)(weight/((height/100) * (height/100)));
	}
	
	public int calculateBMR()
    {
		
		Double BMR = 0.0;
        Double weight = (double)mWeight;
        Double height = (double)mHeight;
		switch(mGender){
		
		case "M":
		BMR = 66 + ( 13.7 * weight ) + ( 5 * height) - ( 6.8 * mAge );
		break;
		
		case "F":
		BMR = 655 + ( 9.6 * weight ) + ( 1.8 * height) - ( 4.7 * mAge );
		break;
		
		default:
		// TODO error handling
		break;
		
		}	
		
		return BMR.intValue();
		
	}
	



}
