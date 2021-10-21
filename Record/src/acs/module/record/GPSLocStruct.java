package acs.module.record;


import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;

public class GPSLocStruct extends Structure {
	private Location location;
	
	private String mProvider;
    private long mTime = 0l;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private boolean mHasAltitude = false;
    private double mAltitude = 0.0f;
    private boolean mHasSpeed = false;
    private float mSpeed = 0.0f;
    private boolean mHasBearing = false;
    private float mBearing = 0.0f;
    private boolean mHasAccuracy = false;
    private float mAccuracy = 0.0f;
    private Bundle mExtras = null;
	
	public GPSLocStruct(Location location){
		super(recordType.GPS);
		this.location=location;
		setGenericParams();
		if(location!=null){
			set(location);
		}
		else{
			mTime=System.nanoTime();
		}
	}
	
	public void set(Location l) {
        mProvider = l.getProvider();
        mTime = System.nanoTime();
        mLatitude = l.getLatitude();
        mLongitude = l.getLongitude();
        mHasAltitude = l.hasAltitude();
        mAltitude = l.getAltitude();
        mHasSpeed = l.hasSpeed();
        mSpeed = l.getSpeed();
        mHasBearing = l.hasBearing();
        mBearing = l.getBearing();
        mHasAccuracy = l.hasAccuracy();
        mAccuracy = l.getAccuracy();
        mExtras = (l.getExtras() == null) ? null : new Bundle(l.getExtras());
    }

	public Location getLocation() {
		return location;
	}

	public String getmProvider() {
		return mProvider;
	}

	public long getmTime() {
		return mTime;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public boolean getmHasAltitude() {
		return mHasAltitude;
	}

	public double getmAltitude() {
		return mAltitude;
	}

	public boolean getmHasSpeed() {
		return mHasSpeed;
	}

	public float getmSpeed() {
		return mSpeed;
	}

	public boolean getmHasBearing() {
		return mHasBearing;
	}

	public float getmBearing() {
		return mBearing;
	}

	public boolean getmHasAccuracy() {
		return mHasAccuracy;
	}

	public float getmAccuracy() {
		return mAccuracy;
	}

	public Bundle getmExtras() {
		return mExtras;
	}
}
