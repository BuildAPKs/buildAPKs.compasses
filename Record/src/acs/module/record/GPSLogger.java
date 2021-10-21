package acs.module.record;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Environment;
import android.util.Log;

public class GPSLogger {
	static int numThread=0;
	private String recordFile;
	ConcurrentLinkedQueue<GPSLocStruct> currentQueue=null;
	ConcurrentLinkedQueue<GPSLocStruct> pingQueue=new ConcurrentLinkedQueue<GPSLocStruct>();
	ConcurrentLinkedQueue<GPSLocStruct> pongQueue=new ConcurrentLinkedQueue<GPSLocStruct>();
	boolean hasCleanedUp=false; 
	public static volatile boolean stop=false;
	int uid;
	FileWriter fileOut=null;
	BufferedWriter out = null;
	public GPSLogger(int uid,String name)
	{
		this.recordFile=Environment.getExternalStorageDirectory()+"/"+name+"_recTempGPSFile";
		this.uid=uid;
		Log.v("LOGGER","GPSLogger created");
		try
		{
			/*out = new ObjectOutputStream(fileOut);*/
			setCurrentQueue(pingQueue);

		}catch(Exception e){
			Log.getStackTraceString(e);
		}
	}

	/*public File getLoggerFile()
	{
		return recordFile;
	}*/


	public  void log(GPSLocStruct struct)
	{	
		if(struct==null){
			stop=true;
			Thread thread1=new Thread(new Runnable(){
				@Override
				public void run() {
					hasCleanedUp=true;
					cleanup(pingQueue);

				}
			});
			thread1.start();
			Thread thread2=new Thread(new Runnable(){
				@Override
				public void run() {
					hasCleanedUp=true;
					cleanup(pongQueue);

				}
			});
			thread2.start();
			try {
				thread1.join();
				thread2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.v("LOGGER", e.toString());
			} 



		}
		else{
			getCurrentQueue().offer(struct);
			Thread thread;
			if((getCurrentQueue().size()>1000)&&(getOtherQueue().isEmpty())){
				toggleCurrentQueue();
				String name=getCurrentQueue()==pingQueue?"ping":"pong";
				Log.v("LOGGER","Should Write "+name);
				thread=new Thread(new Runnable(){
					@Override
					public void run() {
						hasCleanedUp=true;
						cleanup(getOtherQueue());

					}
				});
				thread.start();
			}
		}
	}

	private ConcurrentLinkedQueue<GPSLocStruct> getOtherQueue(){
		return getCurrentQueue()==pingQueue?pongQueue:pingQueue;
	}

	public void toggleCurrentQueue() 
	{
		try{
			if(currentQueue!=null)
			{
				Log.v("LOGGER", "toggling currentqueue");
				if(currentQueue.equals(pingQueue))
				{
					setCurrentQueue(pongQueue);

				}
				else if(currentQueue.equals(pongQueue))
				{
					setCurrentQueue(pingQueue);

				}
			}
		}catch(Exception e)
		{
			Log.e("LOGGER",e.toString());
		}
	}


	public void setCurrentQueue(ConcurrentLinkedQueue<GPSLocStruct> currentQueue)
	{
		try
		{
			this.currentQueue=currentQueue;
			Log.v("LOGGER","setting currentqueue");

		}catch(Exception e)
		{
			Log.e("LOGGER",e.toString());
		}
	}

	public ConcurrentLinkedQueue<GPSLocStruct> getCurrentQueue()
	{
		return this.currentQueue;
	}


	public synchronized void cleanup(ConcurrentLinkedQueue<GPSLocStruct> queue)
	{

		Log.v("LOGGER","cleaning up...");

		try
		{ 	fileOut = new FileWriter(new File(recordFile+Integer.toString(numThread++)+".txt"));
		out=new BufferedWriter(fileOut);
		while(!queue.isEmpty())
		{
			GPSLocStruct ob=queue.poll();
			persist(ob,out);

		}
		out.close();
		Log.v("LOGGER","writing to disk");
		}catch(IOException e)
		{
			Log.e("LOGGER",e.toString());
		}

	}

	private void persist(GPSLocStruct ob,BufferedWriter out) throws IOException{
		out.append(Integer.toString(ob.getLogId())+" ");
		out.append(ob.getMethodName()+" ");
		out.append(Long.toString(ob.getmTime())+" ");
		out.append(ob.getmProvider()+" ");
		if(ob.getmHasAccuracy()){
			out.append(Float.toString(ob.getmAccuracy())+" ");
		}
		else{
			out.append("none"+" ");
		}
		if(ob.getmHasAltitude()){
			out.append(Double.toString(ob.getmAltitude())+" ");
		}
		else{
			out.append("none"+" ");
		}
		if(ob.getmHasBearing()){
			out.append(Float.toString(ob.getmBearing())+" ");
		}
		else{
			out.append("none"+" ");
		}
		if(ob.getmHasSpeed()){
			out.append(Float.toString(ob.getmSpeed())+" ");
		}
		else{
			out.append("none"+" ");
		}
		out.append(Double.toString(ob.getmLatitude())+" ");
		out.append(Double.toString(ob.getmLongitude())+" ");
		if(ob.getmExtras()!=null){
			out.append(ob.getmExtras().toString()+" ");
		}
		else{
			out.append("none");
		}
		out.append("\n");
		
	}




}
