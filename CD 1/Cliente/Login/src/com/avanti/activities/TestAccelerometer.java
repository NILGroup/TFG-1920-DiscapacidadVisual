package com.avanti.activities;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class TestAccelerometer extends Activity implements SensorEventListener{
	
	private float[] magnetic_angles;
	private float[] accelerations;	
	private TextView accX;
	private TextView accY;
	private TextView accZ;
	private TextView giroX;
	private TextView giroY;
	private TextView giroZ;
	private float giroSobreX;
	private float giroSobreY;
	private float giroSobreZ;
	private TextView giroValido;
	private TextView accXNorm;
	private TextView accYNorm;
	private TextView accZNorm;
	private TextView accXMapa;
	private TextView accYMapa;
	private SensorManager mSensorManager;
	private boolean esGiroValido;
	
	private SensorEventListener giroscopio;
	
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.testaccelerometrer);
		accX = (TextView) findViewById(R.id.txtAccX);
		accY = (TextView) findViewById(R.id.txtAccY);
		accZ = (TextView) findViewById(R.id.txtAccZ);
		giroX = (TextView) findViewById(R.id.txtGiroX);
		giroY = (TextView) findViewById(R.id.txtGiroY);
		giroZ = (TextView) findViewById(R.id.txtGiroZ);
		giroValido = (TextView) findViewById(R.id.txtGiroValido);
		accXNorm = (TextView) findViewById(R.id.txtAccXSuelo);
		accYNorm = (TextView) findViewById(R.id.txtAccYSuelo);
		accZNorm = (TextView) findViewById(R.id.txtAccZSuelo);
		accXMapa = (TextView) findViewById(R.id.txtAccXMapa);
		accYMapa = (TextView) findViewById(R.id.txtAccYMapa);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	
		//Sensor s = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		Sensor s = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		Sensor s3 = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		
		giroscopio = new SensorEventListener(){

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
								
			}

			public void onSensorChanged(SensorEvent event) {
				magnetic_angles = event.values;
				/*giroX.setText("" + event.values[1]);
				giroY.setText("" + event.values[2]);
				giroZ.setText("" + event.values[0]);
				if(event.values[2]>40 && ((event.values[1] <40 || event.values[1]>-40) ||(event.values[1] <10 || event.values[1]>170))){
					giroValido.setText("SI");
				}else{
					giroValido.setText("NO");
				}*/
			}
			
		};
		
		mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(giroscopio, s2, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(giroscopio, s3, SensorManager.SENSOR_DELAY_NORMAL);
		
		
		/*Timer t = new Timer("HiloTemporizador");
		TimerTask task = new TimerTask(){

			public void run() {
		
			}
		};
		t.schedule(task, 100, 100);*/
		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		accelerations = event.values;
		accX.setText("" + event.values[0]);
		accY.setText("" + event.values[1]);
		accZ.setText("" + event.values[2]);

		if(magnetic_angles!=null){
			float[] R = new float[9];
			//float[] R2 = new float[9];
			float[] I = new float[9];
			float[] result = new float[3];
			//float aux;
			if(SensorManager.getRotationMatrix(R, I, accelerations, magnetic_angles)){
				//float inclination = (float)((SensorManager.getInclination(I)*180)/Math.PI);
				//accZNorm.setText("" + inclination);
				//SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Y, R2);
//				float[] I2 = new float[9];
//				float[] R2 = new float[9];
				//SensorManager.remapCoordinateSystem(I, SensorManager.AXIS_X, SensorManager.AXIS_Z, I2);
				//SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, R2);
				SensorManager.getOrientation(R, result);
				
				giroSobreX = (float) Math.toDegrees(result[1]);
				giroX.setText("" + giroSobreX);
				giroSobreY= (float) Math.toDegrees(result[2]);
				//giroSobreY = SensorManager.getInclination(I);
				giroY.setText("" + giroSobreY);
				giroSobreZ = (float) Math.toDegrees(result[0]);
				
				
				giroZ.setText("" + giroSobreZ);
			}
			
			if((giroSobreX>=-20 && giroSobreX<=20) && (giroSobreY<=-50 && giroSobreY>=-130)){
				esGiroValido = true;
				giroValido.setText("SI");
			}else{
				esGiroValido = false;
				giroValido.setText("NO");
			}
			
			//Calculo la aceleraci�n en el eje X quitando lo correspondiente a la gravedad
			
			float newAngle = 0;
			
			newAngle = 90 + giroSobreY;
			
			double radians = Math.toRadians(newAngle);
			double accAux = 0;
			
			if(accelerations[0]>0){
				accAux = accelerations[0] - Math.cos(radians)* SensorManager.STANDARD_GRAVITY;
			}else{
				accAux = accelerations[0] + Math.cos(radians)* SensorManager.STANDARD_GRAVITY;
			}
			if(esGiroValido){
				accXNorm.setText("" + accAux);
			}else{
				accXNorm.setText("-");
			}
			
			
			
			
			//Calculo la aceleraci�n de frente
			
			
			
			//Calculo la aceleraci�n en el eje Y
			/*
			if(magnetic_angles[1]<=0){
				if(magnetic_angles[1] >= -90){//0 <= angle >= -90
					newAngle = 90 + magnetic_angles[1];
				}else{//-90 > angle >= -180
					newAngle = -magnetic_angles[1] - 90;
				}
			}else{
				if(magnetic_angles[1]<=90){//0 <= angle <= 90
					newAngle = 90 - magnetic_angles[1];
				}else{// 90 < angle <= 180
					newAngle = magnetic_angles[1] - 90;
				}
			}*/
			//Para usarlo m�s adelante
			/*float angleX = newAngle;
			
			radians = (newAngle*Math.PI)/180;
			
			
			if(event.values[1]>0){
				accAux = event.values[1] - Math.cos(radians)* 9.8;
			}else{
				accAux = event.values[1] + Math.cos(radians)* 9.8;
			}
			accYNorm.setText("" + accAux);*/
			
			
			
			
			//Calculo la aceleraci�n en el eje Z combinando los giros de X e Y
			
			if(giroSobreY<=0 && giroSobreY>=-90){
				newAngle = -giroSobreY;
			}else{
				newAngle = 180 - Math.abs(giroSobreY);
			}
			double radiansY = Math.toRadians(newAngle);
			
		//	float aux2 = (float) Math.abs(Math.toRadians(giroSobreX));
			
			if(accelerations[2]>0){
				accAux = accelerations[2] - Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;
				
			}else{
				accAux = accelerations[2] +  Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;
			}
			accZNorm.setText("" + accAux);
			if(accAux>1){
				int pepe = 1;
				accYNorm.setText("" + pepe);
			}
			if(esGiroValido){
				
				//Redirijo el �ngulo de Z por el giro del m�vil en modo landscape
				giroSobreZ = giroSobreZ + 90;
				if(giroSobreZ > 180) giroSobreZ = giroSobreZ - 360;
				//En el caso de que la aceleraci�n sea negativa invierto su direcci�n
				if(accAux<0){
					if(giroSobreZ<0){
						giroSobreZ = giroSobreZ + 180;
					}else {
						giroSobreZ = giroSobreZ - 180;
					}
					accAux = -accAux;
				}
				
				//Calculo las aceleraciones en los ejes X e Y del mapa  
				
				/*boolean accXPositiva;
				boolean accYPositiva;*/
				double accX;
				double accY;
				if(giroSobreZ>=-180 && giroSobreZ<=-90){
					newAngle = giroSobreZ + 180;
					/*accXPositiva = true;
					accYPositiva = false;*/
					accX = accAux * Math.sin(Math.toRadians(newAngle));
					accY = -(accAux * Math.cos(Math.toRadians(newAngle)));
				}else if(giroSobreZ>-90 && giroSobreZ <=0){
					newAngle = giroSobreZ + 90;
					/*accXPositiva = true;
					accYPositiva = true;*/
					accX = accAux * Math.cos(Math.toRadians(newAngle));
					accY = accAux * Math.sin(Math.toRadians(newAngle));
				}else if(giroSobreZ>0 && giroSobreZ <=90){
					newAngle = giroSobreZ;
					/*accXPositiva = false;
					accYPositiva = true;*/
					accY = accAux * Math.cos(Math.toRadians(newAngle));
					accX = -(accAux * Math.sin(Math.toRadians(newAngle)));
				}else{
					newAngle = giroSobreZ - 90;
					/*accXPositiva = false;
					accYPositiva = false;*/
					accX = -(accAux * Math.cos(Math.toRadians(newAngle)));
					accY = -(accAux * Math.sin(Math.toRadians(newAngle)));
				}
				accXMapa.setText("" + accX);
				accYMapa.setText("" + accY);
			}else{
				accZNorm.setText("-");
			}
			
		}	
	}
	
	public void onDestroy(){
		mSensorManager.unregisterListener(giroscopio);
		mSensorManager.unregisterListener(this);
		super.onDestroy();
	}
	
}
