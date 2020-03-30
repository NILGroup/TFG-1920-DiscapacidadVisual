package com.example.app_guia_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.content.ActivityNotFoundException;
import android.speech.RecognizerIntent;


import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    TextView textgraba;
    SearchView barra_busqueda = null;
    ArrayList<String> listaDestinos = new ArrayList<String>(
            Arrays.asList(
                    "aula 1", "aula 2", "aula 3", "aula 4", "aula 5",
                    "aula 6", "aula 7", "aula 8", "aula 9", "aula 10",
                    "aula 11", "aula 12", "aula 13", "aula 14", "aula 15",
                    "aula 16", "sala de grados", "sala de juntas", "salon de actos", "cafeteria",
                    "cafeteria trasera", "puerta principal", "secretaria", "conserjeria", "biblioteca"
                    ));

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinos);

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

        barra_busqueda = (SearchView) findViewById(R.id.searchView2);

        /*textgraba = findViewById(R.id.txtGrabarVoz);
        recyclerView = (RecyclerView) findViewById(R.id.rvDestinos);*/

        //recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        //creamos el adaptador del recycler agregamos la lista de notas y el onIntemClickListener
        /*recyclerAdaptador = new RecyclerAdaptor(listaDestinos, new RecyclerAdaptor.OnItemClickListener() {
            @Override
            //Obtenemos la posicion
            public void onItemClick(final int position) {
                Toast.makeText(getApplicationContext(),"posiciòn "+position,Toast.LENGTH_LONG).show();
            }
        });
        //Agregamos el adaptador al recycler
        recyclerView.setAdapter(recyclerAdaptador);*/

        aulas_button.setOnClickListener(this);
        cafeteria_button.setOnClickListener(this);
        biblioteca_button.setOnClickListener(this);
        secretaria_button.setOnClickListener(this);
        conserjeria_button.setOnClickListener(this);
        puerta_principal_button.setOnClickListener(this);
        sala_juntas_button.setOnClickListener(this);
        salon_actos_button.setOnClickListener(this);
        sala_grados_button.setOnClickListener(this);

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

    public boolean validDest(String dest){

        if(listaDestinos.contains(dest))
            return true;

        return false;
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
            case R.id.secretaria_button:
                startActivity(ScanningActivity.createIntent(this, "secretaria"));
                break;
            case R.id.conserjeria_button:
                startActivity(ScanningActivity.createIntent(this, "conserjeria"));
                break;
            case R.id.biblioteca_button:
                startActivity(ScanningActivity.createIntent(this, "biblioteca"));
                break;
            case R.id.puerta_principal_button:
                startActivity(ScanningActivity.createIntent(this, "puerta principal"));
                break;
            case R.id.sala_grados_button:
                startActivity(ScanningActivity.createIntent(this, "sala de grados"));
                break;
            case R.id.sala_juntas_button:
                startActivity(ScanningActivity.createIntent(this, "sala de juntas"));
                break;
            case R.id.salon_actos_button:
                startActivity(ScanningActivity.createIntent(this, "salon de actos"));
                break;
            case R.id.cafeteria_button:
                startActivity(ScanningActivity.createIntent(this, "cafeteria"));
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
