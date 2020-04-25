package com.example.app_guia_v5;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_guia_v5.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaAulasActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaAulasActivity.class);
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
        setContentView(R.layout.activity_lista_aulas);

        //Text to speech
        ttsManager = new TTSManager();
        ttsManager.init(this);

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

        Resources res = getResources();
        // Convert String Array to List
        listaDestinos  = Arrays.asList(res.getStringArray(R.array.destinos_array));

        //Ponemos el texto a los botones según el nombre de los destinos
        //Es lo mismo que hacerlo desde la interfaz
        /*aula1_button.setText(listaDestinos.get(0));
        aula2_button.setText(listaDestinos.get(1));
        aula3_button.setText(listaDestinos.get(2));
        aula4_button.setText(listaDestinos.get(3));
        aula5_button.setText(listaDestinos.get(4));
        aula6_button.setText(listaDestinos.get(5));
        aula7_button.setText(listaDestinos.get(6));
        aula8_button.setText(listaDestinos.get(7));
        aula9_button.setText(listaDestinos.get(8));
        aula10_button.setText(listaDestinos.get(9));
        aula11_button.setText(listaDestinos.get(10));
        aula12_button.setText(listaDestinos.get(11));
        aula14_button.setText(listaDestinos.get(12));
        aula14_button.setText(listaDestinos.get(13));
        aula15_button.setText(listaDestinos.get(14));
        aula16_button.setText(listaDestinos.get(15));*/
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
                    else{
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

    @Override
    public void onClick(View view) { //botones normales
        switch (view.getId()) {
            case R.id.aula_1_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 1"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(0)));
                break;
            case R.id.aula_2_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 2"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(1)));
                break;
            case R.id.aula_3_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 3"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(2)));
                break;
            case R.id.aula_4_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 4"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(3)));
                break;
            case R.id.aula_5_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 5"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(4)));
                break;
            case R.id.aula_6_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 6"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(5)));
                break;
            case R.id.aula_7_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 7"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(6)));
                break;
            case R.id.aula_8_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 8"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(7)));
                break;
            case R.id.aula_9_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 9"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(8)));
                break;
            case R.id.aula_10_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 10"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(9)));
                break;
            case R.id.aula_11_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 11"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(10)));
                break;
            case R.id.aula_12_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 12"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(11)));
                break;
            case R.id.aula_13_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 13"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(12)));
                break;
            case R.id.aula_14_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 14"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(13)));
                break;
            case R.id.aula_15_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 15"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(14)));
                break;
            case R.id.aula_16_button:
                //startActivity(ScanningActivity.createIntent(this, "aula 16"));
                startActivity(ScanningActivity.createIntent(this, listaDestinos.get(15)));
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
        else{
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
