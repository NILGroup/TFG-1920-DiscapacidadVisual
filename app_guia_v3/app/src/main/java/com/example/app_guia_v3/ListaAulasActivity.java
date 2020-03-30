package com.example.app_guia_v3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListaAulasActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaAulasActivity.class);
    }
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    private SearchView barra_busqueda = null;

    private static final String ORIGINAL = "áéíóú";
    private static final String REPLACEMENT = "aeiou";

    private static ArrayList<String> listaDestinos = new ArrayList<String>(
            Arrays.asList(
                    "aula 1", "aula 2", "aula 3", "aula 4", "aula 5",
                    "aula 6", "aula 7", "aula 8", "aula 9", "aula 10",
                    "aula 11", "aula 12", "aula 13", "aula 14", "aula 15",
                    "aula 16", "sala de grados", "sala de juntas",
                    "salon de actos",
                    "cafeteria",
                    "cafeteria trasera", "puerta principal",
                    "secretaria",
                    "conserjeria",
                    "biblioteca"
            ));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aulas);

        setupButtons();
    }

    private void setupButtons() {
        final Button aula1_button =  findViewById(R.id.aula_1_button);
        final Button aula2_button =  findViewById(R.id.aula_2_button);
        final Button aula3_button =  findViewById(R.id.aula_3_button);
        final Button aula4_button =  findViewById(R.id.aula_4_button);
        final Button aula5_button =  findViewById(R.id.aula_5_button);
        final Button aula6_button =  findViewById(R.id.aula_6_button);
        final Button aula7_button =  findViewById(R.id.aula_7_button);
        final Button aula8_button =  findViewById(R.id.aula_8_button);
        final Button aula9_button =  findViewById(R.id.aula_9_button);
        final Button aula10_button =  findViewById(R.id.aula_10_button);
        final Button aula11_button =  findViewById(R.id.aula_11_button);
        final Button aula12_button =  findViewById(R.id.aula_12_button);
        final Button aula13_button =  findViewById(R.id.aula_13_button);
        final Button aula14_button =  findViewById(R.id.aula_14_button);
        final Button aula15_button =  findViewById(R.id.aula_15_button);
        final Button aula16_button =  findViewById(R.id.aula_16_button);

        barra_busqueda = (SearchView) findViewById(R.id.aulas_searchView);

        aula1_button.setOnClickListener(this);
        aula2_button.setOnClickListener(this);
        aula3_button.setOnClickListener(this);
        aula4_button.setOnClickListener(this);
        aula5_button.setOnClickListener(this);
        aula6_button.setOnClickListener(this);
        aula7_button.setOnClickListener(this);
        aula8_button.setOnClickListener(this);
        aula9_button.setOnClickListener(this);
        aula10_button.setOnClickListener(this);
        aula11_button.setOnClickListener(this);
        aula12_button.setOnClickListener(this);
        aula13_button.setOnClickListener(this);
        aula14_button.setOnClickListener(this);
        aula15_button.setOnClickListener(this);
        aula16_button.setOnClickListener(this);

        barra_busqueda.setOnQueryTextListener(this);
        barra_busqueda.setQueryHint("Introduce el destino");
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
                        //startActivity(ScanningActivity.createIntent(this, strSpeech2Text));
                    }
                    else{ //Mensaje con destino no valido, habrá que hacerlo por voz
                        Toast.makeText(getApplicationContext(),
                                "El destino introducido no es valido",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
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
        return new String(array);
    }

    @Override
    public void onClick(View view) { //botones normales
        switch (view.getId()) {
            case R.id.aula_1_button:
                startActivity(ScanningActivity.createIntent(this, "aula 1"));
                break;
            case R.id.aula_2_button:
                startActivity(ScanningActivity.createIntent(this, "aula 2"));
                break;
            case R.id.aula_3_button:
                startActivity(ScanningActivity.createIntent(this, "aula 3"));
                break;
            case R.id.aula_4_button:
                startActivity(ScanningActivity.createIntent(this, "aula 4"));
                break;
            case R.id.aula_5_button:
                startActivity(ScanningActivity.createIntent(this, "aula 5"));
                break;
            case R.id.aula_6_button:
                startActivity(ScanningActivity.createIntent(this, "aula 6"));
                break;
            case R.id.aula_7_button:
                startActivity(ScanningActivity.createIntent(this, "aula 7"));
                break;
            case R.id.aula_8_button:
                startActivity(ScanningActivity.createIntent(this, "aula 8"));
                break;
            case R.id.aula_9_button:
                startActivity(ScanningActivity.createIntent(this, "aula 9"));
                break;
            case R.id.aula_10_button:
                startActivity(ScanningActivity.createIntent(this, "aula 10"));
                break;
            case R.id.aula_11_button:
                startActivity(ScanningActivity.createIntent(this, "aula 11"));
                break;
            case R.id.aula_12_button:
                startActivity(ScanningActivity.createIntent(this, "aula 12"));
                break;
            case R.id.aula_13_button:
                startActivity(ScanningActivity.createIntent(this, "aula 13"));
                break;
            case R.id.aula_14_button:
                startActivity(ScanningActivity.createIntent(this, "aula 14"));
                break;
            case R.id.aula_15_button:
                startActivity(ScanningActivity.createIntent(this, "aula 15"));
                break;
            case R.id.aula_16_button:
                startActivity(ScanningActivity.createIntent(this, "aula 16"));
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
            //startActivity(ScanningActivity.createIntent(this, dest));
        }
        else{ //Mensaje con destino no valido, habrá que hacerlo por voz
            Toast.makeText(getApplicationContext(),
                    "El destino introducido no es valido",
                    Toast.LENGTH_SHORT).show();
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
