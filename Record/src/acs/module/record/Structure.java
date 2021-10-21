package acs.module.record;


import java.io.Serializable;

import android.util.Log;


public class Structure  {
	private static final long serialVersionUID = 1L;
	long timeStamp;
	int pid;
	int tid;
	String methodName;
	int logId;
	static int idGen=0;
	String intent;
	recordType type;
	public static enum recordType{
		CAMERA,ACCELEROMETER,GYROSCOPE,CLICK,INTENT,CONTENTPROVIDER,GPS;
	}
	

	public Structure()
	{
		this.type=Structure.recordType.INTENT;
	}

	public Structure(recordType type) {
		this.type=type;
	}
	public recordType getType() {
		return this.type;
	}

	public void setType(recordType type) {
		this.type = type;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}


	public synchronized static int genId(){
		return idGen++;
	}
	
	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}
	
	public boolean setGenericParams()
	{
		try{
			this.setTimeStamp(System.currentTimeMillis());
			this.setTid(android.os.Process.myTid());
			this.setPid(android.os.Process.myPid());
			this.setLogId(Structure.genId());
			
			return true;
		}catch(Exception e)
		{
			Log.e("setallparams", e.toString());
		}
		return false;
	}

	
	
}
