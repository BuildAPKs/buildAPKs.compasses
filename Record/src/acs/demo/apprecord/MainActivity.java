package acs.demo.apprecord;


import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import acs.module.record.ClickLogger;
import acs.module.record.ClickStruct;
import acs.module.record.FileOutStream;
import acs.module.record.GPSLocStruct;
import acs.module.record.GPSLogger;
import acs.module.record.SensorEventStruct;
import acs.module.record.SensorLogger;
import acs.module.record.Structure;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	protected LocationManager locationManager;
	private ImageView image;
	private float currentDegree = 0f;
	private SensorManager mSensorManager;
	private TextView tv;
	private float[] mMagnetData=new float[3];
	private float[] mAccData=new float[3];
	private WakeLock wl;
	private LocationListener l;
	Object lock=new Object();
	boolean toggle=true;
	static long time; //required for record
	static int resumeCounter=0; //required for record
	ClickLogger clickLogger; //required for record
	GPSLogger gpsLogger;  //required for record
	SensorLogger sensorLogger; //required for record
	long sensorCount=0l;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//start add with soot

		prepareLogger();
		//end add with soot

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNjfdhotDimScreen");
		image = (ImageView) findViewById(R.id.imageViewCompass);
		image.setBackgroundColor(color.darker_gray);

		tv = (TextView) findViewById(R.id.textView1);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//start add with soot
				ClickStruct struct=new ClickStruct();
				struct.setLogId(Structure.genId());
				struct.setViewId("button1");
				struct.setTimeStamp(System.nanoTime());
				struct.setTid(android.os.Process.myTid());
				struct.setPid(android.os.Process.myPid());
				struct.setMethodName("setOnClickListener");
				clickLogger.log(struct);
				//end add with soot

				if(l!=null){
					locationManager.removeUpdates(l);
				}
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 
						MINIMUM_TIME_BETWEEN_UPDATES, 
						MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
						l=new MyLocationListener()
						);
				showCurrentLocation();
			}
		});

		findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//start add with soot 
				ClickStruct struct=new ClickStruct();
				struct.setLogId(Structure.genId());
				struct.setViewId("button3");
				struct.setTimeStamp(System.nanoTime());
				struct.setTid(android.os.Process.myTid());
				struct.setPid(android.os.Process.myPid());
				struct.setMethodName("setOnClickListener");
				clickLogger.log(struct);
				//end add with soot
				if(toggle){
					image.setImageResource(R.drawable.black);
					//tv.setBackgroundColor(Color.BLUE);
					toggle = false;
				}
				else{
					image.setImageResource(R.drawable.img_compass);
					//tv.setBackgroundColor(Color.MAGENTA);
					toggle=true;
				}

			}
		});


	}


	protected void showCurrentLocation() {

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//add with soot
		GPSLocStruct struct=new GPSLocStruct(location);
		struct.setMethodName("LAST_LOC");
		gpsLogger.log(struct);
		//end add with soot
		if (location != null)
		{
			String message = String.format("%1$s :: %2$s",location.getLongitude(), location.getLatitude());
			tv.setText(message+"\n");
		}

	}   

	@Override
	protected void onResume() {
		wl.acquire();
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
			sensorCount++;
			//start Logging....
			SensorEventStruct struct=new SensorEventStruct();
			struct.setLogId(Structure.genId());
			struct.setTid(android.os.Process.myTid());
			struct.setPid(android.os.Process.myPid());
			/*Log.v("FLOAT",Long.toString(event.timestamp)+" "+Integer.toString(event.sensor.getType())+"    "+
			Float.toString(event.values[0])+"   "+Float.toString(event.values[1])+"  "+Float.toString(event.values[2]));*/
			struct.setEventFloat(event.values);
			struct.setMethodName("onSensorChanged");
			struct.setEventAccuracy(event.accuracy);
			struct.setSensor(event.sensor.getType());
			struct.setTimeStamp(event.timestamp);
			sensorLogger.log(struct);
			//end Logging

			float azimuth=0f;
			if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
				for(int i=0;i<3;i++){
					mMagnetData[i]=event.values[i];
				}
			}
			else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				for(int i=0;i<3;i++){
					mAccData[i]=event.values[i];
				}	
			}
			else{
				return;
			}
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mAccData,
					mMagnetData);
			if(success){
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuth = (float) Math.toDegrees(orientation[0]);
				azimuth = (azimuth + 360) % 360;
			}
			else{
				return;
			}
			/*Log.v("comp","currdeg: "+Float.toString(currentDegree)+" azimuth: "+Float.toString(azimuth)+
					" "+Float.toString(mAccData[0])+" "+Float.toString(mAccData[1])+" "+Float.toString(mAccData[2])+" "+
					Float.toString(mMagnetData[0])+" "+Float.toString(mMagnetData[1])+" "+Float.toString(mMagnetData[2])+" "+
					event.sensor.getName()+" "+Float.toString(event.values[0])+" "+Float.toString(event.values[1])+" "+Float.toString(event.values[2]));*/
			RotateAnimation ra = new RotateAnimation(
					-currentDegree, 
					-azimuth,
					Animation.RELATIVE_TO_SELF, 0.5f, 
					Animation.RELATIVE_TO_SELF,
					0.5f);

			ra.setDuration(500);

			ra.setFillAfter(true);

			image.startAnimation(ra);
			currentDegree=azimuth;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onBackPressed(){
		if(l!=null){
			locationManager.removeUpdates(l);
		}
		mSensorManager.unregisterListener(this);
		//Log time..and clean up...
		endLogger();
		Log.v("LOGCOUNT",Long.toString(sensorCount));
		//end log and other stuff...

		if(wl.isHeld()){
			wl.release();
		}
		Intent intent=new Intent(this,UploadActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	private void prepareLogger(){
		try {

			File dir=Environment.getExternalStorageDirectory();
			File[] toDelete = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					//System.out.println("LOGGER: File filter name "+name);
					return name.equals("click.txt")||name.equals("time.txt")||name.equals("gps.txt")||name.equals("sensor.txt");
				}
			});

			for(File f:toDelete){
				f.delete();
			}

			FileWriter timeFileWriter=new FileWriter(Environment.getExternalStorageDirectory()+"/"+"time.txt",true);
			timeFileWriter.append("start_time "+Long.toString(System.nanoTime())+" onCreate "+"MainActivity"+"\n");
			timeFileWriter.close();
		} catch (IOException e) {
			Log.v("Logger",e.toString());
		}

		// start add with soot 
		clickLogger=new ClickLogger(android.os.Process.myUid(),"MainActivity-"+Integer.toString(resumeCounter));
		gpsLogger=new GPSLogger(android.os.Process.myUid(),"MainActivity-"+Integer.toString(resumeCounter));
		sensorLogger=new SensorLogger(android.os.Process.myUid(),"MainActivity-"+Integer.toString(resumeCounter));
	}

	private void endLogger(){
		try {
			FileWriter timeFileWriter=new FileWriter(Environment.getExternalStorageDirectory()+"/"+"time.txt",true);
			timeFileWriter.append("end_time "+Long.toString(System.nanoTime())+" onCreate "+"MainActivity"+"\n");
			timeFileWriter.close();
		} catch (IOException e) {
			Log.v("Logger",e.toString());
		}
		sensorLogger.log(null);
		gpsLogger.log(null);
		clickLogger.log(null);
		try {
			FileWriter clickFile=new FileWriter(Environment.getExternalStorageDirectory()+"/"+"click.txt",true);
			FileWriter gpsFile=new FileWriter(Environment.getExternalStorageDirectory()+"/"+"gps.txt",true);
			FileWriter sensorFile=new FileWriter(Environment.getExternalStorageDirectory()+"/"+"sensor.txt");
			File dir=Environment.getExternalStorageDirectory();
			File[] clickFiles = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					System.out.println("LOGGER: File filter name "+name);
					return name.startsWith("MainActivity-0_recTempClick");
				}
			});

			File[] gpsFiles = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("MainActivity-0_recTempGPS");
				}
			});

			File[] sensorFiles = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("MainActivity-0_recTempSensor");
				}
			});

			for(File f:clickFiles){
				FileOutStream outSream=new FileOutStream(f);
				while(outSream.hasNext()){
					clickFile.append(outSream.next()+"\n");
				}
				f.delete();
			}
			clickFile.close();

			for(File f:gpsFiles){
				FileOutStream outSream=new FileOutStream(f);
				while(outSream.hasNext()){
					gpsFile.append(outSream.next()+"\n");
				}
				f.delete();
			}
			gpsFile.close();

			for(File f:sensorFiles){
				FileOutStream outSream=new FileOutStream(f);
				while(outSream.hasNext()){
					sensorFile.append(outSream.next()+"\n");
				}
				f.delete();
			}
			sensorFile.close();

		} catch (IOException e) {
			Log.v("LOGGER",e.toString());
		}
	}

	private class MyLocationListener implements LocationListener 
	{

		public void onLocationChanged(Location location) {
			//Start Logging...

			GPSLocStruct struct=new GPSLocStruct(location);
			struct.setMethodName("LOC");
			gpsLogger.log(struct);

			//End Logging...

			String provider = location.getProvider(); 
			Log.v("PROVIDER",provider);
			String message = String.format("%1$s :: %2$s",location.getLongitude(), location.getLatitude());
			tv.setText(message+"\n");
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(MainActivity.this, "Provider status changed",Toast.LENGTH_SHORT).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(MainActivity.this,"Provider disabled by the user. GPS turned off",Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(MainActivity.this,"Provider enabled by the user. GPS turned on",Toast.LENGTH_SHORT).show();
		}

	}

}