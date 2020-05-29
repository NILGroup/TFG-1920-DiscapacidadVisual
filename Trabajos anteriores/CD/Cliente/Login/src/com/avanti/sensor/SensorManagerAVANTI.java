package com.avanti.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorManagerAVANTI {
	
	private float direction;
	private float xMovement;
	private float yMovement;
	
	public SensorManagerAVANTI(Context ctx) {
		
		SensorEventListener listener = new SensorEventListener(){

			   public void onAccuracyChanged(Sensor arg0, int direction)
			   {
			//	   arg0.
				   
			   }

			   public void onSensorChanged(SensorEvent evt)
			   {
			      float vals[] = evt.values;   
			      
			      direction = vals[0];
			      xMovement = vals[1];
			      yMovement = vals[2];
			      
			    
			   }
			};
			
		final SensorManager sensorMan;
		sensorMan = (SensorManager)ctx.getSystemService(Context.SENSOR_SERVICE);
		      sensorMan.registerListener(
		         listener, 
		         sensorMan.getDefaultSensor(
		        		 SensorManager.SENSOR_ORIENTATION), 
		        	     SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public float getDirection(){
		return direction;
	}
	
	public float getxMovement(){
		return xMovement;
	}
	
	public float getyMovement(){
		return yMovement;
	}
};

