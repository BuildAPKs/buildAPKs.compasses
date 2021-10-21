package acs.module.record;



import java.io.Serializable;



public  class SensorEventStruct extends Structure implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		float[] eventFloat;
		int eventAccuracy;
		int sensor;
		
		public SensorEventStruct()
		{
			super(recordType.ACCELEROMETER);

		}
		public SensorEventStruct(recordType type)
		{
			super(type);
		}
		
		public int getSensor() {
			return this.sensor;
		}
		public void setSensor(int sensor) {
			this.sensor = sensor;
		}
		public float[] getEventFloat() {
			return eventFloat;
		}
		public void setEventFloat(float[] eventFloat) {
			this.eventFloat=new float[3];
			for(int i=0;i<3;i++){
				this.eventFloat[i]=eventFloat[i];
			}
		}
		public int getEventAccuracy() {
			return eventAccuracy;
		}
		public void setEventAccuracy(int eventAccuracy) {
			this.eventAccuracy = eventAccuracy;
		}

	}
