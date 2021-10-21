package acs.module.record;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Environment;
import android.util.Log;

public class ClickLogger {

	static int numThread=0;
	private String recordFile;
	ConcurrentLinkedQueue<ClickStruct> currentQueue=null;
	ConcurrentLinkedQueue<ClickStruct> pingQueue=new ConcurrentLinkedQueue<ClickStruct>();
	ConcurrentLinkedQueue<ClickStruct> pongQueue=new ConcurrentLinkedQueue<ClickStruct>();
	boolean hasCleanedUp=false; 
	public static volatile boolean stop=false;
	int uid;
	FileWriter fileOut=null;
	BufferedWriter out = null;
	public ClickLogger(int uid,String name)
	{
		this.recordFile=Environment.getExternalStorageDirectory()+"/"+name+"_recTempClickFile";
		this.uid=uid;
		Log.v("LOGGER","Logger created");
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

	
	public  void log(ClickStruct ClickStruct)
	{	
		if(ClickStruct==null){
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
			getCurrentQueue().offer(ClickStruct);
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
	
	private ConcurrentLinkedQueue<ClickStruct> getOtherQueue(){
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


	public void setCurrentQueue(ConcurrentLinkedQueue<ClickStruct> currentQueue)
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

	public ConcurrentLinkedQueue<ClickStruct> getCurrentQueue()
	{
		return this.currentQueue;
	}


	public synchronized void cleanup(ConcurrentLinkedQueue<ClickStruct> queue)
	{

		Log.v("LOGGER","cleaning up...");

		try
		{ 	fileOut = new FileWriter(new File(recordFile+Integer.toString(numThread++)+".txt"));
			out=new BufferedWriter(fileOut);
			while(!queue.isEmpty())
			{
				ClickStruct ob=queue.poll();
				persist(ob, out);

			}
			out.close();
			Log.v("LOGGER","writing to disk");
		}catch(IOException e)
		{
			Log.e("LOGGER",e.toString());
		}

	}
	
	
    private void persist(ClickStruct obj,BufferedWriter outBuff) throws IOException{
    	Log.v("LOGGER","persisting...");
    	outBuff.append(Integer.toString(obj.logId)+" "+Long.toString(obj.timeStamp)+" "+obj.viewId+"\n");
	}

    



}
