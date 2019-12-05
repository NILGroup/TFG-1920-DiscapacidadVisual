package com.example.miniapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    Button farButton;
    Button immediateButton;
    Button nearButton;
    Button unknownButton;

    Button farButton2;
    Button immediateButton2;
    Button nearButton2;
    Button unknownButton2;

    Button farButton3;
    Button immediateButton3;
    Button nearButton3;
    Button unknownButton3;

    EditText editText;
    EditText editText2;
    EditText editText3;

    //Array de botones
    Button buttons_array[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        //Setup buttons
        setupButtons();

        //Initialize and configure proximity manager
        setupProximityManager();
    }


    private void setupButtons() {
        Button startScanButton =  findViewById(R.id.start_scan_button);
        Button stopScanButton =  findViewById(R.id.stop_scan_button);

        farButton =  findViewById(R.id.FAR_button);
        immediateButton =  findViewById(R.id.IMMEDIATE_button);
        nearButton =  findViewById(R.id.NEAR_button);
        unknownButton =  findViewById(R.id.UNKNOWN_button);

        editText = findViewById(R.id.beacon_text);

        farButton2 =  findViewById(R.id.FAR_button2);
        immediateButton2 =  findViewById(R.id.IMMEDIATE_button2);
        nearButton2 =  findViewById(R.id.NEAR_button2);
        unknownButton2 =  findViewById(R.id.UNKNOWN_button2);

        editText2 = findViewById(R.id.beacon_text2);

        farButton3 =  findViewById(R.id.FAR_button3);
        immediateButton3 =  findViewById(R.id.IMMEDIATE_button3);
        nearButton3 =  findViewById(R.id.NEAR_button3);
        unknownButton3 =  findViewById(R.id.UNKNOWN_button3);

        editText3 = findViewById(R.id.beacon_text3);

        buttons_array = new Button[12];

        buttons_array[0] = farButton;
        buttons_array[1] = immediateButton;
        buttons_array[2] = nearButton;
        buttons_array[3] = unknownButton;

        buttons_array[4] = farButton2;
        buttons_array[5] = immediateButton2;
        buttons_array[6] = nearButton2;
        buttons_array[7] = unknownButton2;

        buttons_array[8] = farButton3;
        buttons_array[9] = immediateButton3;
        buttons_array[10] = nearButton3;
        buttons_array[11] = unknownButton3;


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
                .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(5));

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
                editText.setText("beacon: " + eddystones.get(0).getUniqueId());
                if(eddystones.size() > 1){
                    editText2.setText("beacon: " + eddystones.get(1).getUniqueId());
                    if(eddystones.size() > 2)
                        editText2.setText("beacon: " + eddystones.get(2).getUniqueId());
                }
                for(int i = 0; i < eddystones.size(); i++) {
                    setProximity(i, eddystones.get(i).getProximity());
                }
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e(TAG, "onEddystoneLost: " + eddystone.toString());
            }
        };
    }

    void setProximity(int i, Proximity p){
        int ini = 0;

        if (i == 1) ini = 4;
        else if (i == 2) ini = 8;

        if(p== Proximity.FAR){
            buttons_array[ini].setBackgroundColor(Color.GREEN);
            buttons_array[ini+1].setBackgroundColor(Color.GRAY);
            buttons_array[ini+2].setBackgroundColor(Color.GRAY);
            buttons_array[ini+3].setBackgroundColor(Color.GRAY);
        }
        else if(p== Proximity.IMMEDIATE){
            buttons_array[ini].setBackgroundColor(Color.GRAY);
            buttons_array[ini+1].setBackgroundColor(Color.GREEN);
            buttons_array[ini+2].setBackgroundColor(Color.GRAY);
            buttons_array[ini+3].setBackgroundColor(Color.GRAY);
        }
        else if(p== Proximity.NEAR){
            buttons_array[ini].setBackgroundColor(Color.GRAY);
            buttons_array[ini+1].setBackgroundColor(Color.GRAY);
            buttons_array[ini+2].setBackgroundColor(Color.GREEN);
            buttons_array[ini+3].setBackgroundColor(Color.GRAY);
        }
        else{
            buttons_array[ini].setBackgroundColor(Color.GRAY);
            buttons_array[ini+1].setBackgroundColor(Color.GRAY);
            buttons_array[ini+2].setBackgroundColor(Color.GRAY);
            buttons_array[ini+3].setBackgroundColor(Color.GREEN);
        }
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
