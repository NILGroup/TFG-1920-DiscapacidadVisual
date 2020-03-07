package com.example.pruebaSonido;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.example.pruebaSonido.routes.Cuadrante;
//import com.example.pruebaSonido.xml.Edificio;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.common.Proximity;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import android.widget.EditText;
import java.io.File;

import 	android.text.method.ScrollingMovementMethod;

import android.media.SoundPool;
import 	android.media.AudioManager;
import java.util.HashMap;
import android.content.Context;
import java.lang.Object;
import android.media.SoundPool.OnLoadCompleteListener;

public class ScanningActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ScanningActivity.class);
    }

    private ProximityManager proximityManager;

    public static final String TAG = "ProximityManager";

    private EditText editText;

    private int scanSeg = 0;

    public static final int sound_1 = R.raw.confirm;
    private static SoundPool soundPool;
    //private static HashMap soundPoolMap;
    boolean loaded = false;
    boolean closest_found = false;

    int soundID;
    String closest_id;
    int pos_closest_id;

    //private static Edificio edificio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        Log.i(TAG, "antes de llamar a new edificio");
        //File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\pruebaSonido\\app\\xml\\edificio.xml" );
        //Log.i(TAG, "DESPUES DE ABRIR ARCHIVOOOO");
        //Log.i("EDIFICIO", "En EDIFICIO"+xmlFile.getName());
        //edificio = new Edificio();

        //Setup buttons
        setupButtons();

        //Initialize and configure proximity manager
        setupProximityManager();

        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });

        soundID = soundPool.load(this, R.raw.confirmmp3, 1);

    }


    private void setupButtons() {
        Button startScanButton =  findViewById(R.id.start_scan_button);
        Button stopScanButton =  findViewById(R.id.stop_scan_button);

        editText = findViewById(R.id.beacon_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        startScanButton.setOnClickListener(this);
        stopScanButton.setOnClickListener(this);
    }

    private void setupProximityManager() {
        proximityManager = ProximityManagerFactory.create(this);

        //Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED)
                //OnDeviceUpdate callback will be received with 5 seconds interval
                .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(2));

        //Setting up iBeacon and Eddystone listeners
        //proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());
    }

    private void startScanning() {
        //Connect to scanning service and start scanning when ready
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                //Check if proximity manager is already scanning
                if (proximityManager.isScanning()) {
                    Toast.makeText(ScanningActivity.this, "Already scanning", Toast.LENGTH_SHORT).show();
                    return;
                }
                proximityManager.startScanning();
                Toast.makeText(ScanningActivity.this, "Scanning started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
            Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private EddystoneListener createEddystoneListener() {
        return new EddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystoneDiscovered: " + eddystone.toString());
            }

            @Override
            public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {
                //Log.i(TAG, "onEddystonesUpdated: " + eddystones.size());
                //int y = (editText.getLineCount() - 1) * editText.getLineHeight(); // the " - 1" should send it to the TOP of the last line, instead of the bottom of the last line
                //editText.scrollTo(0, y);
                //editText.append("______ seg: " + scanSeg +" _________" + "\n");

                /*editText.setText(editText.getText()+ "______ seg: " + scanSeg +" _________" + "\n");
                scanSeg = scanSeg+2;

                for(int i=0; i < eddystones.size();i++){
                    /*editText.append("beacon: " + eddystones.get(i).getUniqueId() + " " +
                            eddystones.get(i).getProximity() + "\n"); //
                    double distance = (double)Math.round(eddystones.get(i).getDistance() * 1000d) / 1000d;
                    editText.setText(editText.getText()+ "beacon: " + eddystones.get(i).getUniqueId() + " " +
                            eddystones.get(i).getProximity() + " a " + distance +" m" + "\n");
                }
                //editText.append("_______________" + "\n");
                editText.setText(editText.getText() + "_______________" + "\n");*/




                if(!closest_found) {
                    double closest_distance = 100;
                    for (int i = 0; i < eddystones.size(); i++) {
                        double distance = (double) Math.round(eddystones.get(i).getDistance() * 1000d) / 1000d;
                        if (distance < closest_distance) {
                            closest_distance = distance;
                            closest_id = eddystones.get(i).getUniqueId();
                            closest_found = true;
                            pos_closest_id = i;
                        }
                    }
                }
                editText.setText(editText.getText() + "El beacon más cercano tiene id: " + closest_id +"\n");

                if(eddystones.get(pos_closest_id).getUniqueId() == closest_id){
                    if(eddystones.get(pos_closest_id).getProximity() == Proximity.FAR){
                        editText.setText(editText.getText() + closest_id + " está LEJOS" +"\n");
                        reproduceSonido(0.3f); //pasando los parametros de frecuencia
                    }
                    else if(eddystones.get(pos_closest_id).getProximity() == Proximity.NEAR){
                        editText.setText(editText.getText() + closest_id + " está CERCA" +"\n");
                        reproduceSonido(1f); //pasando los parametros de frecuencia
                    }
                    else {
                        editText.setText(editText.getText() + closest_id + " está INMEDIATO" +"\n");
                        reproduceSonido(2f); //pasando los parametros de frecuencia
                    }
                }






                //para que haga scroll
                if (editText.getLayout() != null) {
                    final int scrollAmount = editText.getLayout().getLineTop(editText.getLineCount()) - editText.getHeight();
                    // if there is no need to scroll, scrollAmount will be <=0
                    if (scrollAmount > 0)
                        editText.scrollTo(0, scrollAmount);
                    else
                        editText.scrollTo(0, 0);
                }
                //codigo del cuadrante
                /*for(Cuadrante cuadrante: edificio.getCuadrantes()){
                    if(cuadrante.pertenece(eddystones)) {
                        editText.setText(editText.getText() + "Cuadrante: " + cuadrante.getID()
                                + "\n");
                    }
                }*/
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystoneLost: " + eddystone.toString());
            }
        };
    }


    /*private final void focusOnView(){
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.sc
                        smoothScrollTo(0, editText.getBottom());
            }
        });
    }*/

    public void reproduceSonido(float frec){

        //editText.setText(editText.getText() + "Debería estar reproduciendo" + "\n");

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;


        // Is the sound loaded already?
        if(loaded){
            soundPool.play(soundID, volume, volume, 1, 10, frec);
            //editText.setText(editText.getText() + "Debería haber cargado" + "\n");
        }

    }
    public void escribeResultados(){
        Log.i(TAG, editText.getText().toString() + "\n");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_scan_button:
                startScanning();
                break;
            case R.id.stop_scan_button:
                stopScanning();
                escribeResultados();
                break;
        }
    }
    @Override
    protected void onStop() {
        //Stop scanning when leaving screen.
        stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Remember to disconnect when finished.
        proximityManager.disconnect();
        super.onDestroy();
    }

}
