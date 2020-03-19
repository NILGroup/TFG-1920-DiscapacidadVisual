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


public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    TextView textgraba;

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
        //ImageButton mic =  findViewById(R.id.img_btn_hablar);
        textgraba = findViewById(R.id.txtGrabarVoz);

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
                    String strSpeech2Text = speech.get(0);
                    textgraba.setText(strSpeech2Text);
                }
                break;
            default:
                break;
        }
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
