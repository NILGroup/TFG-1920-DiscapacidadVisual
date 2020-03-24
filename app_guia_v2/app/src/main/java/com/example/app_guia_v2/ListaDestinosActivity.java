package com.example.app_guia_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.*;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    TextView textgraba;
    ArrayList<String> listaDestinos = new ArrayList<String>(
            Arrays.asList(
                    "aula 1", "aula 2", "aula 3", "aula 4", "aula 5",
                    "aula 6", "aula 7", "aula 8", "aula 9", "aula 10",
                    "aula 11", "aula 12", "aula 13", "aula 14", "aula 15",
                    "aula 16", "sala de grados", "sala de juntas", "salon de actos", "cafeteria",
                    "cafeteria trasera", "puerta principal", "secretaria", "conserjeria", "biblioteca"
                    ));

    RecyclerAdaptor recyclerAdaptador;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinos);

        setupButtons();
    }

    private void setupButtons() {
        /*Button aula_13_button =  findViewById(R.id.aula_6_button);
        Button aula_6_button =  findViewById(R.id.aula_13_button);
        Button lab_button =  findViewById(R.id.lab_button);
        Button aula_x_button =  findViewById(R.id.cuad_19_button);*/
        textgraba = findViewById(R.id.txtGrabarVoz);
        recyclerView = (RecyclerView) findViewById(R.id.rvDestinos);

        recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        //creamos el adaptador del recycler agregamos la lista de notas y el onIntemClickListener
        recyclerAdaptador = new RecyclerAdaptor(listaDestinos, new RecyclerAdaptor.OnItemClickListener() {
            @Override
            //Obtenemos la posicion
            public void onItemClick(final int position) {
                Toast.makeText(getApplicationContext(),"posiciòn "+position,Toast.LENGTH_LONG).show();
            }
        });
        //Agregamos el adaptador al recycler
        recyclerView.setAdapter(recyclerAdaptador);

        /*aula_13_button.setOnClickListener(this);
        aula_6_button.setOnClickListener(this);
        lab_button.setOnClickListener(this);
        aula_x_button.setOnClickListener(this);*/

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
       /* switch (view.getId()) {
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
        }*/
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_buscador,menu);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(this);

        item.setOnActionExpandListener( new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item){

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
                recyclerAdaptador.setFilter(listaDestinos);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try{
            ArrayList<String> listaFiltrada = filter(listaDestinos, newText);
            recyclerAdaptador.setFilter(listaFiltrada);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<String> filter(ArrayList<String> listaDest, String texto){
        ArrayList<String> listaFiltrada = new ArrayList<>();

        try{
            texto = texto.toLowerCase();

            for(String d: listaDest){
                if(d.contains(texto)){//Si lo que escribo esta en la lista
                    listaFiltrada.add(d);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }


}
