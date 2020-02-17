package com.example.cuadrantes_v1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cuadrantes_v1.routes.Cuadrante;
import com.example.cuadrantes_v1.xml.Edificio;
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

public class ScanningActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ScanningActivity.class);
    }

    private ProximityManager proximityManager;

    public static final String TAG = "ProximityManager";

    private EditText editText;

    private static Edificio edificio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        Log.i(TAG, "antes de llamar a new edificio");
        edificio = new Edificio();

        //Setup buttons
        setupButtons();

        //Initialize and configure proximity manager
        setupProximityManager();
    }


    private void setupButtons() {
        Button startScanButton =  findViewById(R.id.start_scan_button);
        Button stopScanButton =  findViewById(R.id.stop_scan_button);

        editText = findViewById(R.id.beacon_text);

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
                editText.setText(editText.getText()+ "beacon: " + eddystones.get(0).getUniqueId() + " " +
                        eddystones.get(0).getProximity() + "\n");
                if(eddystones.size() > 1){
                    editText.setText(editText.getText()+ "beacon: " + eddystones.get(1).getUniqueId()+ " " +
                            eddystones.get(1).getProximity()+ "\n");
                }
                if(eddystones.size() > 2)
                    editText.setText(editText.getText()+ "beacon: " + eddystones.get(2).getUniqueId()+ " " +
                            eddystones.get(2).getProximity()+ "\n");
                //codigo del cuadrante
                for(Cuadrante cuadrante: edificio.getCuadrantes()){
                    if(cuadrante.pertenece(eddystones)) {
                        editText.setText(editText.getText() + "Cuadrante: " + cuadrante.getID()
                                + "\n");
                    }
                }
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystoneLost: " + eddystone.toString());
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_scan_button:
                startScanning();
                break;
            case R.id.stop_scan_button:
                stopScanning();
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
