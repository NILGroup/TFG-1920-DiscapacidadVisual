package com.example.app_guia_v5;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import android.widget.ImageButton;

public class ScanningActivity extends AppCompatActivity  implements View.OnClickListener {

    private ProximityManager proximityManager;
    public static final String TAG = "ProximityManager";
    private List<String> uri; //uri del servidor

    private EditText editText;
    private TTSManager ttsManager = null;
    private MediaPlayer mp;
    Vibrator vibrator;

    private int scanSeg = 0;

    private static String destino;

    private boolean hayRuta = false, hayServ = false, modoSilencio = false;
    private String origen;
    private String beacon_mas_cerca;

    private List<String> listaInstrucciones, listaBeacons, listaGiros, listaInfoAdicional;
    private Integer indiceRuta = 0, numPasosPerdidos = 0;
    private static boolean verbose = true; //Por defecto esta a true
    private Button iniciar_button, modo_verb_button, stop_button, repet_button, mute_button;

    //private PowerManager.WakeLock wakeLock;

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


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        //wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BlindBit::WakelockTag");
    }


    private void setupButtons() {
        editText = findViewById(R.id.beacon_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        iniciar_button = (Button) findViewById(R.id.iniciar_button);
        iniciar_button.setBackgroundColor(Color.parseColor("#58028B"));
        iniciar_button.setOnClickListener(this);

        modo_verb_button = (Button) findViewById(R.id.modo_verb_ruta_button);
        modo_verb_button.setBackgroundColor(Color.parseColor("#CC95ED"));
        modo_verb_button.setOnClickListener(this);

        stop_button = (Button) findViewById(R.id.parar_button);
        stop_button.setOnClickListener(this);

        repet_button = (Button) findViewById(R.id.repetir_button);
        repet_button.setOnClickListener(this);

        mute_button = (Button) findViewById(R.id.mute_button);
        mute_button.setOnClickListener(this);

        Resources res = getResources();
        // Convert String Array to List
        uri  = Arrays.asList(res.getStringArray(R.array.uri_servidor));
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
        //wakeLock.acquire();
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
            //Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
        //wakeLock.release();
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
                    //numPasosPerdidos = 0 ;
                    String[] results = new String[4];
                    if (!hayRuta) { //Es la primera vez que se llama al servidor
                        Log.i(TAG, "Si no hay ruta ya");
                        //Hay que saber el origen
                        origen = beacon_mas_cerca;

                        indiceRuta = 0;

                        //Preguntamos al cliente la info de la ruta
                        conectaCliente();
                        Log.i(TAG, "userLost: " + "despues de conectaCliente:" + hayServ);

                        if(hayServ) {//Si no ha habido ningún problema en la conexión con el servidor
                            hayRuta = true;

                            indicaInstruccion();
                            escribeEditText();
                            indiceRuta++;
                        }

                    } else {//Solo actualizamos la posición actual y llamamos al servidor cuando estamos en el cuadrante clave
                        Log.i(TAG, "userLost: " + "hayRuta: " + hayRuta);

                        if (beacon_mas_cerca.equals(listaBeacons.get(indiceRuta))) {
                            numPasosPerdidos = 0; //Ha encontrado el siguiente paso
                            //como ha llegado al beaconClave salta el sonido de acierto
                            mp.start();
                            indicaInstruccion();
                            escribeEditText();
                            indiceRuta++;
                        }
                        else{//No estamos en el beacon clave
                            numPasosPerdidos++;
                            Log.i(TAG, "userLost: " + "no estamos en el beacon clave: " + numPasosPerdidos);
                        }
                    }
                    Log.i(TAG, "userLost: " + "numPasosPerdidos: " + numPasosPerdidos);

                    if(hayServ && numPasosPerdidos >= 10){//Consideramos que el usuario se ha perdido
                        Log.i(TAG, "userLost: " + "El usuario se ha perdido");
                        numPasosPerdidos = 0;
                        int indiceBmasCerca = indiceBeacon(beacon_mas_cerca);
                        if( indiceBmasCerca != -1 && indiceBmasCerca >= indiceRuta){//El usuario sigue en la ruta
                            //Volvemos a conectar con el servidor para que nos de las instrucciones
                            //de por donde vamos. El usuario va en la dirección correcta.
                            //hayRuta = false;
                            //hayServ = false;
                            indiceRuta = indiceBmasCerca;
                            Log.i(TAG, "userLost: " + "en el if");
                        }
                        else{//El usuario se ha salido de la ruta
                            Log.i(TAG, "userLost: " + "en el else");
                            ttsManager.addQueue("La dirección tomada no ha sido la correcta. " +
                                    "Da la vuelta para volver en la dirección en la que venías. La nueva ruta comenzará cuando pulses " +
                                    "iniciar ruta." );
                            editText.setText("La dirección tomada no ha sido la correcta. " +
                                    "Da la vuelta para volver en la dirección en la que venías. La nueva ruta comenzará cuando pulses " +
                                    "iniciar ruta.");
                            hayRuta = false;
                            hayServ = false;
                            //Se para de escanear y se espera a que el usuario vuelva a iniciar la ruta
                            stopScanning();
                            iniciar_button.setBackgroundColor(Color.parseColor("#58028B"));
                        }
                    }

                    if (hayServ && listaBeacons.get(indiceRuta).equals("FINAL")){
                        indiceRuta--; //Nos quedamos en la última instrucción
                        //Patrón de vibración
                        //long[] pattern={0,100,1000,200,200,100,400,200,100,1000};
                        long[] pattern={0,500,0,500,0,500};
                        vibrator.vibrate(pattern,-1);
                        stopScanning();
                        hayRuta = false;
                    }
                }
                else{
                    numPasosPerdidos++;
                    if(numPasosPerdidos >= 10) {
                        ttsManager.addQueue("Te encuentras fuera del alcance de la aplicación. " +
                                "Dirígete al interior del edificio, la ruta comenzará cuando pulses sobre iniciar ruta.");
                        editText.setText("Te encuentras fuera del alcance de la aplicación" +
                                "Dirígete al interior del edificio, la ruta comenzará cuando pulses sobre iniciar ruta.");
                        stopScanning();
                        iniciar_button.setBackgroundColor(Color.parseColor("#58028B"));
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

    private void indicaInstruccion(){
        ttsManager.addQueue(listaInstrucciones.get(indiceRuta));
        if(verbose){
            if(!listaInfoAdicional.get(indiceRuta).equals("no")) {
                ttsManager.addQueue(listaInfoAdicional.get(indiceRuta));
            }
        }
        if(listaGiros.get(indiceRuta).equals("iz")){
            vibrator.vibrate(1000); //vibración larga
        }
        else if(listaGiros.get(indiceRuta).equals("der")){
            long[] pattern={0,500,0,500}; //dos vibraciones cortas
            vibrator.vibrate(pattern,-1);
        }
    }

    private void escribeEditText(){
        editText.setText(editText.getText() + "______________\n");
        editText.setText(editText.getText() + "Destino: " + destino + "\n");
        editText.setText(editText.getText() + listaInstrucciones.get(indiceRuta) + "\n");
        editText.setText(editText.getText() + "Beacon más cercano: " + beacon_mas_cerca + "\n");
        editText.setText(editText.getText() + "Beacon clave: " + listaBeacons.get(indiceRuta+1) + "\n");
        editText.setText(editText.getText() + "______________\n");
    }

    private void conectaCliente(){
        String[] results = new String[4];
        //Hacemos un hilo que llame al servidor para que nos de los parámetros que queremos
        Cliente c = new Cliente(destino, origen, uri.get(0));
        Log.i("URI", uri.get(0) +"\n");
        results = c.createWebSocketClient().clone();

        if(results[0].equals("noInfo")){
            ttsManager.initQueue("No se ha podido conectar con el servidor");
            editText.setText("No se ha podido conectar con el servidor.");
            hayServ = false;
            onStop();
        }
        else {
            hayServ = true;
            listaBeacons = Arrays.asList(results[0].split(Pattern.quote(" ")));
            listaInstrucciones = Arrays.asList(results[1].split(Pattern.quote("@")));
            listaGiros = Arrays.asList(results[2].split(Pattern.quote("@")));
            listaInfoAdicional = Arrays.asList(results[3].split(Pattern.quote("@")));
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
        //wakeLock.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder popup=new AlertDialog.Builder(this);
        if(hayRuta) {
            popup.setMessage("¿Está seguro de que desea finalizar la ruta?");
            popup.setTitle("Finalizar ruta");
            popup.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onStop();
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
        else {
            onStop();
            finish();
        }
    }

    public static void setVerbose(boolean verb) {
        verbose = verb;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) { //cambiar el de config
            case R.id.iniciar_button:
                //iniciar_button.setBackgroundColor(Color.parseColor("#49A605"));
                iniciar_button.getBackground().setAlpha(128);  // 50% transparent
                ttsManager.addQueue("Iniciando ruta a " + destino);
                startScanning();
                break;
            case R.id.modo_verb_ruta_button: // que cuando se pulse se ponga al contrario de lo que está
                if (verbose) {
                    verbose = false;
                    //modo_verb_button.setAlpha(.5f);
                    modo_verb_button.getBackground().setAlpha(128);  // 50% transparent
                    //modo_verb_button.setBackgroundColor(Color.LTGRAY);
                    ttsManager.addQueue("Funcionalidad instrucciones detalladas desactivada");
                }
                else {
                    verbose = true;
                    //modo_verb_button.setAlpha(.10f);
                    modo_verb_button.setBackgroundColor(Color.parseColor("#CC95ED"));
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

            case R.id.mute_button:
                if(!modoSilencio){
                    modoSilencio = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mute_button.setBackground(getResources().getDrawable(R.drawable.speakeroff));
                        ttsManager.shutDown();
                    }
                }
                else{
                    modoSilencio = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mute_button.setBackground(getResources().getDrawable(R.drawable.speaker));
                        ttsManager.init(this);
                    }
                }
                break;
        }
    }
}
