package com.example.app_guia_v5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.content.ActivityNotFoundException;
import android.speech.RecognizerIntent;


import android.widget.Toast;
import java.util.ArrayList;
import java.util.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_guia_v5.R;

public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    private SearchView barra_busqueda = null;
    private static final String ORIGINAL = "áéíóú";
    private static final String REPLACEMENT = "aeiou";
    private static List<String> listaDestinos;

    private TTSManager ttsManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinos);

        //Text to speech
        ttsManager = new TTSManager();
        ttsManager.init(this);

        setupButtons();
    }

    private void setupButtons() {

        final Button aulas_button =  findViewById(R.id.aulas_button);
        final Button cafeteria_button =  findViewById(R.id.cafeteria_button);
        final Button biblioteca_button =  findViewById(R.id.biblioteca_button);
        final Button secretaria_button =  findViewById(R.id.secretaria_button);
        final Button conserjeria_button =  findViewById(R.id.conserjeria_button);
        final Button puerta_principal_button =  findViewById(R.id.puerta_principal_button);
        final Button sala_juntas_button =  findViewById(R.id.sala_juntas_button);
        final Button salon_actos_button =  findViewById(R.id.salon_actos_button);
        final Button sala_grados_button =  findViewById(R.id.sala_grados_button);

        barra_busqueda = (SearchView) findViewById(R.id.destinos_searchView);

        aulas_button.setOnClickListener(this);
        cafeteria_button.setOnClickListener(this);
        biblioteca_button.setOnClickListener(this);
        secretaria_button.setOnClickListener(this);
        conserjeria_button.setOnClickListener(this);
        puerta_principal_button.setOnClickListener(this);
        sala_juntas_button.setOnClickListener(this);
        salon_actos_button.setOnClickListener(this);
        sala_grados_button.setOnClickListener(this);

        barra_busqueda.setOnQueryTextListener(this);
        barra_busqueda.setQueryHint("Introduce el destino");

        Resources res = getResources();
        // Convert String Array to List
        listaDestinos  = Arrays.asList(res.getStringArray(R.array.destinos_array));

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
                    String strSpeech2Text = cleanString(speech.get(0));

                    //Comprobar que es un destino valido
                    if(listaDestinos.contains(strSpeech2Text)){
                        //llamamos al servidor con ese destino
                        Toast.makeText(getApplicationContext(),
                                strSpeech2Text,
                                Toast.LENGTH_SHORT).show();
                        startActivity(ScanningActivity.createIntent(this, strSpeech2Text));
                    }
                    else{ //Mensaje con destino no valido, habrá que hacerlo por voz
                        /*Toast.makeText(getApplicationContext(),
                                "El destino introducido no es valido",
                                Toast.LENGTH_SHORT).show();*/
                        ttsManager.addQueue("El destino introducido no es válido.");
                    }
                }
                break;
            default:
                break;
        }
    }


    //Quita tildes y mayúsculas
    public String cleanString(String str){

        String str_clean = new String(str).toLowerCase();

        //quitamos las tildes
        char[] array = str_clean.toCharArray();
        for (int index = 0; index < array.length; index++) {
            int pos = ORIGINAL.indexOf(array[index]);
            if (pos > -1) {
                array[index] = REPLACEMENT.charAt(pos);
            }
        }
        str_clean = new String(array);

        //El micrófono de Google funciona mal con estas dos
        if(str_clean.equals("aula1")){
            str_clean = "aula 1";
        }
        else if(str_clean.equals("aula2")){
            str_clean = "aula 2";
        }
        return str_clean;
    }

    //Reconocedor de voz
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
    public void onClick(View view) { //botones normales
        switch (view.getId()) {
            case R.id.aulas_button:
                startActivity(ListaAulasActivity.createIntent(this));
                break;
            case R.id.biblioteca_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(16)));
                break;
            case R.id.cafeteria_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(17)));
                break;
            case R.id.conserjeria_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(18)));
                break;
            case R.id.puerta_principal_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(19)));
                break;
            case R.id.sala_grados_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(20)));
                break;
            case R.id.sala_juntas_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(21)));
                break;
            case R.id.salon_actos_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(22)));
                break;
            case R.id.secretaria_button:
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(23)));
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String dest = cleanString(query);
        if(listaDestinos.contains(dest)){
            //llamamos al servidor con ese destino
            Toast.makeText(getApplicationContext(),
                    dest,
                    Toast.LENGTH_SHORT).show();
            startActivity(ScanningActivity.createIntent(this, dest));
        }
        else{ //Mensaje con destino no valido, habrá que hacerlo por voz
            /*Toast.makeText(getApplicationContext(),
                    "El destino introducido no es valido",
                    Toast.LENGTH_SHORT).show();*/
            ttsManager.addQueue("El destino introducido no es válido.");
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        /*String text = newText;
        adapter.filter(text);*/
        return false;
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
