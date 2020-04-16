package com.example.app_guia_v5;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

import android.widget.EditText;

import 	android.text.method.ScrollingMovementMethod;

public class ScanningActivity extends AppCompatActivity  implements View.OnClickListener {

    private ProximityManager proximityManager;
    public static final String TAG = "ProximityManager";

    private EditText editText;
    private TTSManager ttsManager = null;
    private MediaPlayer mp;
    Vibrator vibrator;

    private int scanSeg = 0;

    private static String destino;

    private boolean hayRuta = false, hayServ = false;
    private String origen;
    private String beacon_mas_cerca;

    private List<String> listaInstrucciones, listaBeacons, listaCuadrantes, listaGiros, listaInfoAdicional;
    private Integer indiceRuta = 0, numPasosPerdidos = 0;
    private static boolean verbose = true; //Por defecto esta a true
    private Button iniciar_button, modo_verb_button, stop_button, repet_button;

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
        //Inicializamos el sonido MediaPlayer
        mp = MediaPlayer.create(this, R.raw.acierto);
        //Inicializamos la vibración
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        //Setup buttons
        setupButtons();
        //Initialize and configure proximity manager
        setupProximityManager();
    }


    private void setupButtons() {
        editText = findViewById(R.id.beacon_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        iniciar_button = (Button) findViewById(R.id.iniciar_button);
        iniciar_button.setBackgroundColor(Color.parseColor("#68EC07"));
        iniciar_button.setOnClickListener(this);

        modo_verb_button = (Button) findViewById(R.id.modo_verb_ruta_button);
        modo_verb_button.setBackgroundColor(Color.parseColor("#F49A06"));
        modo_verb_button.setOnClickListener(this);

        stop_button = (Button) findViewById(R.id.parar_button);
        stop_button.setOnClickListener(this);

        repet_button = (Button) findViewById(R.id.repetir_button);
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
        proximityManager.setEddystoneListener(createEddystoneListener());
    }

    private void startScanning() {
        //Connect to scanning service and start scanning when ready
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                //Check if proximity manager is already scanning
                if (proximityManager.isScanning()) {
                    //Toast.makeText(ScanningActivity.this, "Already scanning", Toast.LENGTH_SHORT).show();
                    return;
                }
                proximityManager.startScanning();
                //Toast.makeText(ScanningActivity.this, "Scanning started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
            //Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
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
                if (!beacon_mas_cerca.equals("NO")) {
                    String[] results = new String[4];
                    if (!hayRuta) { //Es la primera vez que se llama al servidor
                        Log.i(TAG, "Si no hay ruta ya");
                        //Hay que saber el origen
                        origen = beacon_mas_cerca;

                        //Preguntamos al cliente la info de la ruta
                        conectaCliente();

                        if(hayServ) {//Si no ha habido ningún problema en la conexión con el servidor
                            hayRuta = true;
                            Log.i("WebSocket en Scanning", "si hay serv");
                            ttsManager.addQueue("Bienvenido a la Facultad de Informática de la UCM.");
                            ttsManager.addQueue("Iniciando ruta a " + destino);
                            ttsManager.addQueue(listaInstrucciones.get(indiceRuta));
                            if (verbose) {
                                if (!listaInfoAdicional.get(indiceRuta).equals("no")) {
                                    ttsManager.addQueue(listaInfoAdicional.get(indiceRuta));
                                }
                            }
                            if (listaGiros.get(indiceRuta).equals("si")) {
                                vibrator.vibrate(1000);
                            }
                            escribeEditText();
                            indiceRuta++;
                        }
                        Log.i("WebSocket en Scanning", "despues de si hay serv");
                    } else {//Solo actualizamos la posición actual y llamamos al servidor cuando estamos en el cuadrante clave
                        Log.i(TAG, "Si hay ruta ya");
                        if (beacon_mas_cerca.equals(listaBeacons.get(indiceRuta))) {
                            numPasosPerdidos = 0; //Ha encontrado el siguiente paso
                            //como ha llegado al beaconClave salta el sonido de acierto
                            mp.start();
                            Log.i(TAG, "Si hay ruta ya, antes de llamar a cliente");

                            ttsManager.addQueue(listaInstrucciones.get(indiceRuta));
                            if(verbose){
                                if(!listaInfoAdicional.get(indiceRuta).equals("no")) {
                                    ttsManager.addQueue(listaInfoAdicional.get(indiceRuta));
                                }
                            }
                            if(listaGiros.get(indiceRuta).equals("si")){
                                vibrator.vibrate(1000);
                            }
                            escribeEditText();
                            indiceRuta++;
                        }
                        else{//No estamos en el beacon clave
                            numPasosPerdidos++;
                        }
                    }

                    if(hayServ && numPasosPerdidos >= 10){//Consideramos que el usuario se ha perdido
                        numPasosPerdidos = 0;
                        int indiceBmasCerca = indiceBeacon(beacon_mas_cerca);
                        if( indiceBmasCerca != -1 && indiceBmasCerca >= indiceRuta){//El usuario sigue en la ruta
                            //Volvemos a conectar con el servidor para que nos de las instrucciones
                            //de por donde vamos. El usuario va en la dirección correcta.
                            hayRuta = false;
                            hayServ = false;
                        }
                        else{//El usuario se ha salido de la ruta
                            ttsManager.addQueue("La dirección tomada no ha sido la correcta. " +
                                    "Vuelve sobre tus pasos, la nueva ruta comenzará cuando pulses " +
                                    "iniciar ruta." );
                            hayRuta = false;
                            hayServ = false;
                            //Se para de escanear y se espera a que el usuario vuelva a iniciar la ruta
                            stopScanning();
                            iniciar_button.setBackgroundColor(Color.parseColor("#68EC07"));
                        }
                    }

                    if (hayServ && listaBeacons.get(indiceRuta).equals("FINAL")){
                        indiceRuta--; //Nos quedamos en la última instrucción
                        //Patrón de vibración
                        long[] pattern={0,100,1000,200,200,100,400,200,100,1000};
                        vibrator.vibrate(pattern,-1);
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

    private int indiceBeacon(String beacon){
        int index = 0;
        for(String b: listaBeacons){
            if(b.equals(beacon)){
                return index;
            }
            index++;
        }
        return -1;
    }

    private void escribeEditText(){
        editText.setText(editText.getText() + "______________\n");
        editText.setText(editText.getText() + listaInstrucciones.get(indiceRuta) + "\n");
        editText.setText(editText.getText() + "Beacon más cercano: " + beacon_mas_cerca + "\n");
        editText.setText(editText.getText() + "Beacon clave: " + listaBeacons.get(indiceRuta+1) + "\n");
        editText.setText(editText.getText() + "______________\n");
    }

    private void conectaCliente(){
        String[] results = new String[4];
        //Hacemos un hilo que llame al servidor para que nos de los parámetros que queremos
        Cliente c = new Cliente(destino, origen);
        results = c.createWebSocketClient().clone();

        if(results[0].equals("noInfo")){
            ttsManager.initQueue("No se ha podido conectar con el servidor");
            hayServ = false;
            onStop();
        }
        else {
            hayServ = true;
            listaCuadrantes = Arrays.asList(results[0].split(Pattern.quote(" ")));
            listaBeacons = Arrays.asList(results[1].split(Pattern.quote(" ")));
            listaInstrucciones = Arrays.asList(results[2].split(Pattern.quote("@")));
            listaGiros = Arrays.asList(results[3].split(Pattern.quote("@")));
            listaInfoAdicional = Arrays.asList(results[4].split(Pattern.quote("@")));
        }
    }

    public String encuentraElMasCercano(List<IEddystoneDevice> eddystones) {
        String masCercano = "NO";
        double distMin = 20;

        for (int i = 0; i < eddystones.size(); i++) {
            //double distance = (double)Math.round(eddystones.get(i).getDistance() * 1000d) / 1000d;
            double distance = eddystones.get(i).getDistance();

            if (distance < distMin) {
                distMin = distance;
                masCercano = eddystones.get(i).getUniqueId();
            }
        }
        return masCercano;
    }

    public void escribeResultados() {
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder popup=new AlertDialog.Builder(this);
        popup.setMessage("¿Está seguro de que desea finalizar la ruta?");
        popup.setTitle("Finalizar ruta");
        popup.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        popup.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
            }
        });

        AlertDialog dialog = popup.create();
        dialog.show();
      //super.onBackPressed();
    }

    public static void setVerbose(boolean verb) {
        verbose = verb;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) { //cambiar el de config
            case R.id.iniciar_button:
                //iniciar_button.setBackgroundColor(Color.parseColor("#49A605"));
                iniciar_button.setBackgroundColor(Color.GRAY);
                startScanning();
                break;
            case R.id.modo_verb_ruta_button: // que cuando se pulse se ponga al contrario de lo que está
                if (verbose) {
                    verbose = false;
                    modo_verb_button.setBackgroundColor(Color.GRAY);
                    ttsManager.addQueue("Funcionalidad instrucciones detalladas desactivada");
                }
                else {
                    verbose = true;
                    modo_verb_button.setBackgroundColor(Color.parseColor("#F49A06"));
                    ttsManager.addQueue("Funcionalidad instrucciones detalladas activada");
                }
                break;

            case R.id.parar_button:
                onStop();
                //ttsManager.initQueue("Se ha detenido la ruta");
                onBackPressed();
                //startActivity(ListaDestinosActivity.createIntent(this));
                break;

            case R.id.repetir_button:
                if(hayServ) {
                    ttsManager.initQueue(listaInstrucciones.get(indiceRuta));
                    editText.setText(editText.getText() + "______________\n");
                    editText.setText(editText.getText() + "Ruta repetida:" + listaInstrucciones.get(indiceRuta) + "\n");
                    editText.setText(editText.getText() + "______________\n");
                }
                break;
        }
    }
}
