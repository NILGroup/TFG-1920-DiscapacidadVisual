package com.example.app_guia_v4;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import Cuadrante;
//import Edificio;
import com.example.app_guia_v4.R;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import android.widget.EditText;

import 	android.text.method.ScrollingMovementMethod;

public class ScanningActivity extends AppCompatActivity  implements View.OnClickListener {


    private ProximityManager proximityManager;
    public static final String TAG = "ProximityManager";

    private EditText editText;
    private TTSManager ttsManager=null;

    private int scanSeg = 0;

    private static String destino;

    private boolean hayRuta = false;
    private String origen, beaconClave, listaCuadrantes, ruta, rutaFinal;
    private String beacon_mas_cerca;
    private static boolean verbose = true; //Por defecto esta a true

    public static Intent createIntent(@NonNull Context context, String dest) {
        destino = dest;
        return new Intent(context, ScanningActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        //Inicializamos el objeto de la clase TTSManager
        ttsManager = new TTSManager();
        ttsManager.init(this);
        //Setup buttons
        setupButtons();
        //Initialize and configure proximity manager
        setupProximityManager();
        //Start scanning beacons
        startScanning();
    }


    private void setupButtons() {
        editText = findViewById(R.id.beacon_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        Button modo_verb_button = (Button) findViewById(R.id.modo_verb_ruta_button);
        modo_verb_button.setOnClickListener(this);

        Button stop_button = (Button) findViewById(R.id.parar_button);
        stop_button.setOnClickListener(this);

        Button repet_button = (Button) findViewById(R.id.repetir_button);
        repet_button.setOnClickListener(this);
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
                //Buscamos el beacon que está más cerca
                beacon_mas_cerca = encuentraElMasCercano(eddystones);
                Log.i(TAG, "Despues de buscar el mas cercano");
                if(!beacon_mas_cerca.equals("NO")) {
                    String[] results = new String[4];
                    if (!hayRuta) { //Es la primera vez que se llama al servidor
                        Log.i(TAG, "Si no hay ruta ya");
                        hayRuta = true;
                        //Hay que saber el origen
                        origen = beacon_mas_cerca;
                        Log.i(TAG, "Si no hay ruta ya, antes de llamar a cliente");
                        Cliente c = new Cliente(destino, beacon_mas_cerca, origen, verbose);
                        Log.i(TAG, "Si no hay ruta ya, despues de llamar a cliente");
                        //Hacemos un hilo que llame al servidor para que nos de los parámetros que queremos
                        results = c.createWebSocketClient().clone(); //ANTES HABIA UN CLONE
                        Log.i(TAG, "Si no hay ruta ya, despues de llamar a createWebSocketClient");
                        listaCuadrantes = results[0];
                        ruta = results[1]; //OJO QUE UNO DE LOS DOS ES NULL
                        rutaFinal = results[2]; //OJO QUE UNO DE LOS DOS ES NULL
                        beaconClave = results[3];

                        editText.setText(editText.getText() + "______________\n");
                        editText.setText(editText.getText() + ruta + "\n");
                        ttsManager.initQueue(ruta);
                        editText.setText(editText.getText() + "Beacon clave: " + beaconClave + "\n");
                        editText.setText(editText.getText() + "Beacon más cercano: " + beacon_mas_cerca + "\n");
                        editText.setText(editText.getText() + "______________\n");


                    } else {//Solo actualizamos la posición actual y llamamos al servidor cuando estamos en el cuadrante clave
                        Log.i(TAG, "Si hay ruta ya");
                        hayRuta = true;
                        //editText.setText(editText.getText()+ "En el else\n");
                        if (beacon_mas_cerca.equals(beaconClave)) {
                            Log.i(TAG, "Si hay ruta ya, antes de llamar a cliente");
                            //editText.setText(editText.getText()+ "En el if\n");
                            Cliente c = new Cliente(destino, beacon_mas_cerca, origen, verbose);
                            //Hacemos un hilo que llame al servidor para que nos de los parámetros que queremos
                            results = c.createWebSocketClient().clone(); //ANTES HABIA UN CLONE

                            Log.i(TAG, "Si hay ruta ya, despues de llamar a createWebSocketClient");
                            listaCuadrantes = results[0];
                            ruta = results[1]; //OJO QUE UNO DE LOS DOS ES NULL
                            rutaFinal = results[2]; //OJO QUE UNO DE LOS DOS ES NULL
                            beaconClave = results[3];

                            editText.setText(editText.getText() + "______________\n");
                            editText.setText(editText.getText() + ruta + "\n");
                            ttsManager.initQueue(ruta);
                            editText.setText(editText.getText() + "Beacon más cercano: " + beacon_mas_cerca + "\n");
                            editText.setText(editText.getText() + "Beacon clave: " + beaconClave + "\n");
                            editText.setText(editText.getText() + "______________\n");
                        }

                    }
                    if(beaconClave.equals("FINAL")){
                        stopScanning();
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

            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystoneLost: " + eddystone.toString());
            }
        };
    }

    public String encuentraElMasCercano(List<IEddystoneDevice> eddystones){
        String masCercano = "NO";
        double distMin = 20;

        for(int i=0; i < eddystones.size();i++){
            //double distance = (double)Math.round(eddystones.get(i).getDistance() * 1000d) / 1000d;
            double distance = eddystones.get(i).getDistance();

            if (distance < distMin){
                distMin = distance;
                masCercano = eddystones.get(i).getUniqueId();
            }
        }
        return masCercano;
    }

    public void escribeResultados(){
        Log.i(TAG, editText.getText().toString() + "\n");
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
        ttsManager.shutDown();
        super.onDestroy();
    }

    public static void setVerbose(boolean verb){
        verbose = verb;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) { //cambiar el de config
            case R.id.modo_verb_ruta_button: // que cuando se pulse se ponga al contrario de lo que está
                editText.setText(editText.getText()+ "Modo Verb\n");
                if(verbose) verbose = false;
                else verbose = true;
                if (ConfigActivity.getModo_verb_switch() != null) {
                    ConfigActivity.getModo_verb_switch().setChecked(verbose); //con esto se hace bien? el cambio de boton
                    editText.setText(editText.getText()+ "Config: " +toString(ConfigActivity.getModo_verb_switch().isChecked())+ "\n");
                }
                editText.setText(editText.getText()+ "Scanning: " + toString(verbose) +"\n");

                //Toast.makeText(getApplicationContext(),"prueba verbose scanning:" + toString(verbose),Toast.LENGTH_SHORT);
                //Toast.makeText(getApplicationContext(),"prueba verbose config:" + toString(ConfigActivity.getModo_verb_switch().isChecked()),Toast.LENGTH_SHORT);
                break;

            case R.id.parar_button:
                this.onStop();
                startActivity(ListaDestinosActivity.createIntent(this));
                break;

            case R.id.repetir_button:
                editText.setText(editText.getText()+ "______________\n");
                editText.setText(editText.getText()+ "Ruta repetida:" + ruta +"\n");
                ttsManager.initQueue(ruta);
                editText.setText(editText.getText()+ "______________\n");
                break;
        }

    }

    public String toString(boolean verb){
        if(verb) return "true";
        else return "false";
    }
}
