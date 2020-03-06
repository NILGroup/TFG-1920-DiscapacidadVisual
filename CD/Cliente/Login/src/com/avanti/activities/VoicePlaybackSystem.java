package com.avanti.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avanti.routes.ListaCuadrantes;
import com.avanti.sockets.Client;
import com.avanti.wps.Coordenada;
import com.avanti.wps.WPS;
import com.avanti.wps.WPSException;


public class VoicePlaybackSystem extends Activity implements OnInitListener, OnClickListener,SensorEventListener{
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 0x100;
	private static final int REQUEST_CHECK_TTS = 0x1000;

	private int posIni = -1;
	private int posAct = -1;
	private float orientacion = 999;
	private int contSupervisa = 50;
	
	private ListaCuadrantes cuadrantes;
	private WifiManager manager;
	private TextToSpeech mTts;
	private SensorEventListener giroscopio;
	private SensorManager mSensorManager;
//	private EditText mEditText;
			
	private float[] magnetic_angles;
	private float[] accelerations;
	private float giroSobreX;
	private float giroSobreY;
	private float giroSobreZ;
	private boolean esGiroValido;
	
	private boolean movilHorizontal = true;
	private boolean activaWifi = true;
	private boolean wifiActivado;
	private boolean destinoInsertado = false;
	
	
	Button btBuscar,btDestino, btCancel,btVoz;
//	Button btHablar;
	private TextView mResult2;
	private EditText mResult1;
	
	//Atributo para obtener la ruta que me devuelva el servidor
	private String ruta;
	private String siguientePaso;
	private int cuadranteClave;
	private int cuadranteActual;
	private String listaCuadrantesString;
	ArrayList<String> listaCuadrantes;
	private Timer timerCalcularRuta;
	private boolean timerActivado;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playbackvoice);
		
		mResult1 = (EditText) findViewById(R.id.editText1);
		mResult2 = (TextView) findViewById(R.id.textView2);
		
		cuadrantes = new ListaCuadrantes();
		manager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiActivado = manager.isWifiEnabled();
		timerActivado=false;
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor s = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		Sensor s2 = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		giroscopio = new SensorEventListener(){

			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			public void onSensorChanged(SensorEvent event) {
				magnetic_angles = event.values;
			}
			
		};
		
		mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(giroscopio, s2, SensorManager.SENSOR_DELAY_NORMAL);
				
		checkTTS();
      
//		btHablar = (Button) findViewById(R.id.button1);
//      btHablar.setOnClickListener(this);
        
		
		
		btVoz=(Button) findViewById(R.id.bVoz);
		
		btBuscar=(Button) findViewById(R.id.bBuscar);
		btBuscar.setOnClickListener(new OnClickListener() {	
			
			public void onClick(View v) {
				destinoInsertado = true;
				
				consultarRutaServidor();
				calculaRuta();
			}
		});
		
		btCancel=(Button) findViewById(R.id.button3);
		btCancel.setOnClickListener(new OnClickListener() {	
			
			public void onClick(View v) {
				finish();
			}
		}); /*
		
		
		String origen = String.valueOf(8);	
		String actual = String.valueOf(8);	
		String destino = "aula 13";//leerFicheroMemoriaInterna(); 
		String anguloOrientacion = String.valueOf(orientacion);
	
		String []datos = {origen, destino, anguloOrientacion,actual};
		Client c = new Client();
		c.execute(datos);
		try {
			c.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
     }
	
	
	/** Llamada a la función de reconocimiento de voz, obtenemos el destino del usuario */
	public void onButtonClick(View v) {
		if(!hasVoicerec()) {
			Toast.makeText(this, "Este terminal no tiene instalado el soporte de reconocimiento de voz", Toast.LENGTH_LONG).show();
			return;
		}
		final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		final String miPackage = getClass().getPackage().getName();
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, miPackage);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Tu habla que yo escribo");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	
	public boolean hasVoicerec() {
		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> activities = pm.queryIntentActivities(
		  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		return (activities.size() != 0);
	}
	
	
	/** Llamada a la funcion Text To Speech*/
	private void checkTTS() {
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, REQUEST_CHECK_TTS);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//Reconocimiento de voz
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			// TODO hacer lo que sea con las cadenas
			if (matches != null && matches.size() > 0) {
				mResult1.setText(matches.get(0));
				destinoInsertado = true;
				
				consultarRutaServidor();
				calculaRuta();
			} else {
				mResult1.setText("Sin destino");
			}
		super.onActivityResult(requestCode, resultCode, data);
		}
		
		//Reproducción de voz
		if (requestCode == REQUEST_CHECK_TTS) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	 public void onClick(View arg0) {		
		// mEditText = (EditText) findViewById(R.id.editText1);
		// mEditText.setText(String.valueOf(posIni));
	}
	 
	
	/**
	 * Init tts
	 */
	public void onInit(int status) {
		mTts.setLanguage(new Locale("spa"));
	}
	
	@Override
	protected void onDestroy() {
		mSensorManager.unregisterListener(giroscopio);
		mSensorManager.unregisterListener(this);
		mTts.shutdown();
		super.onDestroy();
	}
//
//	/**
//	 * Método que calcula cada 10seg aproximadamente:
//	 * 		1- La orientación del usuario, en grados.
//	 * 		2- La posición inicial del usuario, obtengo el cuadrante en el que está situado.
//	 * Cada 10 segundos mando los datos al servidor, que comprueba que el usuario sigue la ruta que se ha establevido, 
//	 * recalculando la nueva ruta si fuese necesario.
//	 */
	public void onSensorChanged(SensorEvent event) {
//		
//		int posActAux = -1;
//		if (destinoInsertado){
//		if (wifiActivado){
//			if (movilHorizontal){
//				if( contSupervisa>=50){
//					WPS lista = new WPS(this);
//					try {
//						Coordenada c = MainMenu.thread.getDatabase().calculatePosition(lista.scanAndShow());							
//						posActAux = cuadrantes.numCuadrante(c.getX(), c.getY());
//						System.out.println("Pos ini" + String.valueOf(posAct));
//					} catch (WPSException e) {
//						Log.e("ThreadUbicacion", e.getMessage());
//						e.printStackTrace();
//					}
//			
//					accelerations = event.values;
//					if(magnetic_angles!=null){
//						float[] R = new float[9];
//						float[] I = new float[9];
//						float[] result = new float[3];
//						if(SensorManager.getRotationMatrix(R, I, accelerations, magnetic_angles)){
//							SensorManager.getOrientation(R, result);
//							giroSobreX = (float) Math.toDegrees(result[1]);
//							giroSobreY= (float) Math.toDegrees(result[2]);
//							giroSobreZ = (float) Math.toDegrees(result[0]);
//						}
//						if((giroSobreX>=-20 && giroSobreX<=20) && (giroSobreY<=-50 && giroSobreY>=-130)){
//							esGiroValido = true;
//						}
//						else{
//							esGiroValido = false;
//						}
//				
//						//Calculo la aceleración en el eje X quitando lo correspondiente a la gravedad
//				
//						float newAngle = 0;
//						newAngle = 90 + giroSobreY;
//				
//						double radians = Math.toRadians(newAngle);
//						double accAux = 0;
//				
//						if(accelerations[0]>0){
//							accAux = accelerations[0] - Math.cos(radians)* SensorManager.STANDARD_GRAVITY;
//						}
//						else{
//							accAux = accelerations[0] + Math.cos(radians)* SensorManager.STANDARD_GRAVITY;
//						}
//				
//						//Calculo la aceleración en el eje Z combinando los giros de X e Y
//				
//						if(giroSobreY<=0 && giroSobreY>=-90){
//							newAngle = -giroSobreY;
//						}
//						else{
//							newAngle = 180 - Math.abs(giroSobreY);
//						}
//						double radiansY = Math.toRadians(newAngle);
//				
//						if(accelerations[2]>0){
//							accAux = accelerations[2] - Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;	
//						}
//						else{
//							accAux = accelerations[2] +  Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;
//						}
//				
//						if(esGiroValido){
//					
//							//Redirijo el ángulo de Z por el giro del móvil en modo landscape
//							giroSobreZ = giroSobreZ + 90;
//							if(giroSobreZ > 180) 
//								giroSobreZ = giroSobreZ - 360;
//					
//							//En el caso de que la aceleración sea negativa invierto su dirección
//							if(accAux<0){
//								if(giroSobreZ<0){
//									giroSobreZ = giroSobreZ + 180;
//								}
//								else {
//									giroSobreZ = giroSobreZ - 180;
//								}
//								accAux = -accAux;
//							}
//							orientacion = giroSobreZ;
//						}
//					}
//					
//					if( posIni == -1 )
//						posIni = posActAux;
//			
//					if (true){//((orientacion != 999) && (posAct != posActAux) && (posActAux != -1)){
//						posAct = posActAux;	
//				
//						/*Llamada al servidor*/
//						String origen = "5";//String.valueOf(posIni);	
//						String actual = String.valueOf(posAct);	
//						String destino = mResult1.getText().toString();//"aula 13";//leerFicheroMemoriaInterna(); 
//						String anguloOrientacion = String.valueOf(orientacion);
//		  		
//						String []datos = {origen, destino, anguloOrientacion,actual};
//						Client c = new Client();
//						c.execute(datos);
//						try {
//							c.get();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ExecutionException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				
//						/*Obtengo la ruta generada por el servidor, acompañada de un valor:
//						 * 	1 - la ruta se ha recalculado.
//						 *  2 - primera ruta generada.
//						 *  0 - el usuario sigue el camino correcto, no hace falta hacer nada.
//						 */
//						String cuadrantes=c.getCuadrantes();
//						ruta = c.getRuta();
//						
//						Log.d("cua", cuadrantes);
//						
//						try {
//							String text =  "";
//							if (c.getCaminoCorrecto() == 2){	
//								mResult2.setText("La ruta calculada es:\n" + ruta + "\n Ha llegado a su destino");
//								text = "La ruta calculada es " + ruta + "Ha llegado a su destino"; 
//							}
//							else if (c.getCaminoCorrecto() == 1){
//								mResult2.setText("Ruta recalculada, la nueva ruta es:\n " + ruta);
//								text = "Ruta recalculada, la nueva ruta es " + ruta;
//								posIni = posAct;
//							}
//							if (text != "" && c.getCaminoCorrecto() != 0 ){
//								int queueMode = TextToSpeech.QUEUE_ADD;
//								HashMap<String, String> params = null;
//								mTts.speak(text, queueMode, params);
//							}
//
//						}catch (Exception e) {
//							e.printStackTrace();
//					}
//				}
//				else {
//					if (orientacion == 999){
//						movilHorizontal = false;
//						contSupervisa=50;
//						AlertDialog.Builder builder = new AlertDialog.Builder(this);
//						builder.setMessage("Ponga el dispositivo en posición horizontal.");
//						builder.setPositiveButton("Aceptar", new android.content.DialogInterface.OnClickListener(){
//							public void onClick(DialogInterface arg0, int arg1) {
//								movilHorizontal = true;
//						}	
//						});
//						builder.setNegativeButton("Salir", new android.content.DialogInterface.OnClickListener(){
//							public void onClick(DialogInterface dialog, int which) {
//								finish();					
//						}
//						
//						});
//						builder.setCancelable(false);
//						AlertDialog alert = builder.create();
//						alert.show();
//					
//					}
//					else{
//						ruta = "Ha ocurrido un problema en la localización";
//					}
//				}
//				contSupervisa=0;
//				}
//			}
//		}
//		else{
//			if (activaWifi){
//				activaWifi = false;
//				activateWifi();
//			}
//		}
//		contSupervisa++;
//		}
	}
	
	private void activateWifi(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("El dispositivo WiFi está desactivado.");
		
		builder.setPositiveButton("Activar", new android.content.DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				activaWifi = true;
				wifiActivado = true;
				manager.setWifiEnabled(true);
					
				int i = 0;
				boolean b = false;
				while(!b && i < 10){
					b = manager.isWifiEnabled();
					i++;
					try {
						SystemClock.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}	
		});
		builder.setNegativeButton("Salir", new android.content.DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				finish();					
			}
			
		});
		builder.setCancelable(false);
		AlertDialog alert = builder.create();
		alert.show(); 
	}

	public int getPosIni() {
		return posIni;
	}
	
	public void setPosIni(int posIni) {
		this.posIni = posIni;
	}
	
	public float getOrientacion() {
		return orientacion;
	}
	
	public void setOrientacion(float orientacion) {
		this.orientacion = orientacion;
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub	
	}
	
	
	public void calculaRuta(){
		
		if (destinoInsertado){
			if (!wifiActivado){
				activateWifi();
			}
			if(!timerActivado){
				timerActivado=true;
				TimerTask timerTask = new TimerTask() 
			     { 
			         public void run()  
			         { 
			             calculaRuta();
			         } 
			     }; 
			     timerCalcularRuta = new Timer(); 
			     timerCalcularRuta.scheduleAtFixedRate(timerTask, 0, 5000);
			}
			
			consultarRutaServidor();
			if (estaEnLaRuta(cuadranteActual)){
				if (esPuntoClave(cuadranteActual)){
					actualizarSiguientePaso();
				}
			}
			else {
				consultarRutaServidor();
				return;
			}
	
			
			return;
			
			}


	}

	private void actualizarSiguientePaso() {
		consultarRutaServidor();
		
	}


	private boolean esPuntoClave(int cuadranteActual) {
		return cuadranteClave==cuadranteActual;
	}


	public Coordenada obtenerCoordenadaActual(){
		
		WPS lista = new WPS(this);
		try {
			Coordenada c = MainMenu.thread.getDatabase().calculatePosition(lista.scanAndShow());							
			return c;
			
		} catch (WPSException e) {
			Log.e("ThreadUbicacion", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void consultarRutaServidor(){
		/*Llamada al servidor*/
		//String origen = String.valueOf(posIni);	
		//String actual = String.valueOf(posAct);	
		String origenX=String.valueOf(obtenerCoordenadaActual().getX());
		String origenY=String.valueOf(obtenerCoordenadaActual().getY());
		String origenZ=String.valueOf(obtenerCoordenadaActual().getZ());
		String actualX=String.valueOf(obtenerCoordenadaActual().getX());
		String actualY=String.valueOf(obtenerCoordenadaActual().getY());
		String actualZ=String.valueOf(obtenerCoordenadaActual().getZ());
		String destino = mResult1.getText().toString();//"aula 13";//leerFicheroMemoriaInterna(); 
		String anguloOrientacion = String.valueOf(orientacion);
	
		String []datos = {origenX,origenY,origenZ, destino, anguloOrientacion,actualX,actualY,actualZ};
		Client c = new Client();
		c.execute(datos);
		try {
			c.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ruta = c.getRuta();
		listaCuadrantesString=c.getCuadrantes();
		listaCuadrantes=new ArrayList<String>(Arrays.asList(listaCuadrantesString.split(" ")));
		cuadranteClave = c.getCuadranteClave();
		cuadranteActual=Integer.parseInt(listaCuadrantes.get(0));
		//cuadranteActual=listaCuadrantes.indexOf(0);
		String aux=mResult2.getText().toString();
		mResult2.setText(ruta);
		if(!aux.equals(ruta)){
			mTts.speak(ruta, TextToSpeech.QUEUE_FLUSH, null);
		}
		
	}
	
	private boolean estaEnLaRuta(int cuadranteActual) {
		return listaCuadrantes.contains(String.valueOf(cuadranteActual));
	}
}
