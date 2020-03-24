package com.example.app_guia_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.*;


public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    TextView textgraba;
    ArrayList<String> listaDestinos = new ArrayList<String>(
            Arrays.asList("Geeks",
                    "for",
                    "Geeks"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinos);

        setupButtons();
    }

    private void setupButtons() {
        Button aula_13_button =  findViewById(R.id.aula_6_button);
        Button aula_6_button =  findViewById(R.id.aula_13_button);
        Button lab_button =  findViewById(R.id.lab_button);
        Button aula_x_button =  findViewById(R.id.cuad_19_button);
        textgraba = findViewById(R.id.txtGrabarVoz);

        listaDestinos = [];

        aula_13_button.setOnClickListener(this);
        aula_6_button.setOnClickListener(this);
        lab_button.setOnClickListener(this);
        aula_x_button.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Tomamos el texto en minuscula
                    String strSpeech2Text = speech.get(0).toLowerCase();
                    textgraba.setText(strSpeech2Text);
                    //Comprobar que es un destino valido
                    if(validDest(strSpeech2Text)){
                        //llamamos al servidor con ese destino
                        startActivity(ScanningActivity.createIntent(this, strSpeech2Text));
                    }

                }
                break;
            default:
                break;
        }
    }

    public boolean validDest(String dest){

        switch (dest) {
            case "aula 6":
                startActivity(ScanningActivity.createIntent(this, "aula 6"));
                break;
            case "aula 13":
                startActivity(ScanningActivity.createIntent(this, "aula 13"));
                break;
            case "lab":
                break;
            case "aula 7":
                startActivity(ScanningActivity.createIntent(this, "aula x"));
                break;
        }


        return true;
    }

    public void onClickImgBtnHablar(View v) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-España)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aula_6_button:
                startActivity(ScanningActivity.createIntent(this, "aula 6"));
                break;
            case R.id.aula_13_button:
                startActivity(ScanningActivity.createIntent(this, "aula 13"));
                break;
            case R.id.lab_button:
                break;
            case R.id.cuad_19_button:
                startActivity(ScanningActivity.createIntent(this, "aula x"));
                break;
        }
    }
    @Override
    protected void onStop() {
        //Stop scanning when leaving screen.
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Remember to disconnect when finished.
        super.onDestroy();
    }
}
