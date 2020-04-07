package com.example.app_guia_v4;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Vibrator;
import android.view.View;
import android.widget.Switch;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private TTSManager ttsManager;
    MediaPlayer mp;
    Vibrator vibrator;

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ConfigActivity.class);
    }

    private static Switch modo_verb_switch = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializamos el objeto de la clase TTSManager
        ttsManager = new TTSManager();
        ttsManager.init(this);

        mp= MediaPlayer.create(this, R.raw.acierto);
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        setContentView(R.layout.activity_config);
        setupButtons();

    }

    private void setupButtons(){
        Button volume_button = (Button) findViewById(R.id.volumen_button);
        modo_verb_switch = (Switch) findViewById(R.id.modo_verb_switch);

        //set the initial state of verbose
        //modo_verb_switch.setChecked(false);

        volume_button.setOnClickListener(this);
        modo_verb_switch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) { //switches
        switch (view.getId()) {
            case R.id.volumen_button:
                mp.start();
                vibrator.vibrate(1000);
                break;
            case R.id.modo_verb_switch:
                ScanningActivity.setVerbose(modo_verb_switch.isChecked());
                break;
        }
    }
    public static Switch getModo_verb_switch() {
        return modo_verb_switch;
    }

    @Override
    protected void onDestroy() {
       ttsManager.shutDown();
        super.onDestroy();
    }
}
