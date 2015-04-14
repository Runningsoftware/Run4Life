package com.example.tawfiq.run4life;

public class Run {

    private String mDateID;     //ID based on date to sort runs in database
	private double mDistance; //total distance in KM
	private double mTargetDistance; // targeted distance in KM.. if free run this = 0
	private double mNetCalories;    //estimated calorie burn calculated at the end of the run
	private double mAvgSpeed;   //average speed TotalDistance / Duration
    private double mDuration;   //in seconds
	private double mTargetAvgSpeed;
	private double mScore;

	//MyService mService = new MyService();


	//More Running: (http://www.indiacurry.com/weightloss/walkingrunningcalories.htm) 

	// ----------------- get set

    public String getDateID() {return mDateID;}

    public void setDateID(String ID){ mDateID = ID;}

	public double getDistance() {
		return mDistance;
	}

	public void setDistance(double mDistance) {
		this.mDistance = mDistance;
	}

	public double getTargetDistance() {
		return mTargetDistance;
	}

	public void setTargetDistance(double mTargetDistance) {
		this.mTargetDistance = mTargetDistance;
	}

	public double getNetCalories() {
		return mNetCalories;
	}

	public void setNetCalories(double mNetCalories) {
		this.mNetCalories = mNetCalories;
	}

	public double getAvgSpeed() {
		return mAvgSpeed;
	}

	public void setAvgSpeed(double mAvgSpeed) {
		this.mAvgSpeed = mAvgSpeed;
	}

    public double getDuration(){return mDuration;}

    public void setDuration(double duration){mDuration = duration;}

	public double getTargetAvgSpeed() {
		return mTargetAvgSpeed;
	}

	public void setTargetAvgSpeed(double mTargetAvgSpeed) {
		this.mTargetAvgSpeed = mTargetAvgSpeed;
	}

	public double getScore() {
		return mScore;
	}

	public void setScore(double mScore) {
		this.mScore = mScore;
	}


	// ----------------- methods
	
	//public double net_calories_burned(){
		
	//	return  (2.20462)*(mService.getUser().getWeight()) *(0.63) *(0.621371)*(this.mDistance);
	//}
	
	
	/*
	Net Running calories Spent = (Body weight in pounds) x (0.63) x (Distance in miles) 

		A person weighing 160 pounds will burn 100.8 extra calories by running for one mile. 

		Total Running calories Spent = (Body weight in pounds) x (0.75) x (Distance in miles) 

		Walking (http://ask.metafilter.com/48652/Walking-formula) 

		For moderate walking: Calories burned = 0.029 x weight (lbs) x time (minutes) 

		For Vigorous walking: Calories burned = 0.048 x weight (lbs) x time (minutes)
	 */
	


}
