package com.example.app_guia_v5;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO;


public class ListaDestinosDinamicaActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    private SearchView barra_busqueda = null;
    private static final String ORIGINAL = "áéíóú";
    private static final String REPLACEMENT = "aeiou";
    private static int nivel;
    private static String clave_segundonivel;
    private TTSManager ttsManager = null;

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Map<String, List<String>> tablaDestinos; // destinos que leemos del XML
    private List<String> destinos_ordenados; //Array con los nombres de los destinos en el mismo orden del XML

    public static Intent createIntent(@NonNull Context context,int niv, String k) {
        nivel=niv;
        clave_segundonivel=k;
        return new Intent(context, ListaDestinosDinamicaActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinosdinamica);
        tabla = (TableLayout) findViewById(R.id.tabla);
        filas=new ArrayList<TableRow>();
        tablaDestinos= new HashMap<>();

        //Text to speech
        ttsManager = new TTSManager();

        ttsManager.init(this);

        leerDestinos();
        setupButtons();
    }
    private void leerDestinos(){
        Resources res = getResources();
        // Convert String Array to List
        List<String> stringArray  = Arrays.asList(res.getStringArray(R.array.destinosdinamicos_array));
        destinos_ordenados = new ArrayList<String>();

        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            String[] splitResult2;
            if (!splitResult[1].equals("no")) //hay un segundo nivel
                  splitResult2 = splitResult[1].split("\\,");
            else splitResult2= new String[]{"no"}; //no hay segundo nivel

            tablaDestinos.put(splitResult[0], Arrays.asList(splitResult2));
            destinos_ordenados.add(splitResult[0]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setupButtons() {

        barra_busqueda = (SearchView) findViewById(R.id.destinosdinamicos_searchView);
        barra_busqueda.setOnQueryTextListener(this);
        barra_busqueda.setQueryHint("Introduce el destino");
        String[] nombresbotones = new String[destinos_ordenados.size()];
        //primer nivel
        if (nivel==1){
             //nombresbotones = tablaDestinos.keySet().toArray(new String[0]);
            nombresbotones = destinos_ordenados.toArray(nombresbotones); //Para que los destinos salgan en el orden del XML
        }
        //segundo nivel
        else{
            nombresbotones =  tablaDestinos.get(clave_segundonivel).toArray(new String[0]);
        }

        escribeBotones(nombresbotones);

        /*int pos = 0;
        //Arrays.sort(nombresbotones);

        for(int i = 0; i < nombresbotones.length/3; i++)
        {
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add(nombresbotones[pos+1]);
            elementos.add(nombresbotones[pos+2]);
            agregarFilaTabla(elementos, pos);
            pos=pos+3;
        }
        if(nombresbotones.length%3 == 1){
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add("");
            elementos.add("");
            agregarFilaTabla(elementos,pos);
        }
        if(nombresbotones.length%3 == 2){
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add(nombresbotones[pos+1]);
            elementos.add("");
            agregarFilaTabla(elementos,pos);
        }
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void escribeBotones(String [] nombresbotones){
        int pos = 0;
        //Arrays.sort(nombresbotones);

        for(int i = 0; i < nombresbotones.length/3; i++)
        {
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add(nombresbotones[pos+1]);
            elementos.add(nombresbotones[pos+2]);
            agregarFilaTabla(elementos, pos);
            pos=pos+3;
        }
        if(nombresbotones.length%3 == 1){
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add("");
            elementos.add("");
            agregarFilaTabla(elementos,pos);
        }
        if(nombresbotones.length%3 == 2){
            ArrayList<String> elementos=new ArrayList<>();
            elementos.add(nombresbotones[pos]);
            elementos.add(nombresbotones[pos+1]);
            elementos.add("");
            agregarFilaTabla(elementos,pos);
        }
    }

    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void agregarFilaTabla(final ArrayList<String> elementos, int pos)
    {
        //igual como ultimo parametro en los layauts hay que meter un 1
        TableRow.LayoutParams layoutCelda = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            final Button mybutton = new Button(this);
            mybutton.setLayoutParams(layoutCelda);
            mybutton.setText(elementos.get(i));
            Log.i("DINAMICA", "nivel: " + nivel);

            if(!elementos.get(i).equals("")){//si el boton no es vacio
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                     if(pos%2==0){ //fila par ->coloreamos los pares
                         if (i%2 == 0){
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                 mybutton.setBackground(getResources().getDrawable(R.drawable.edit_text_borderandbackground));
                             }
                         }
                         else {
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                 mybutton.setBackground(getResources().getDrawable(R.drawable.edit_text_border_light));
                             }
                         }
                     }
                     else{//fila impar-> coloreamos los impares
                         if (i%2 == 1){
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                 mybutton.setBackground(getResources().getDrawable(R.drawable.edit_text_borderandbackground));
                             }
                         }
                         else {
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                 mybutton.setBackground(getResources().getDrawable(R.drawable.edit_text_border_light));
                             }
                         }
                     }
            }
                mybutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nivel==2)  startActivity(ScanningActivity.createIntent(getApplicationContext(),(String) mybutton.getText()));
                        else{ //estoy en el primer nivel
                            if(tablaDestinos.get(mybutton.getText()).contains("no")) //es un destino final
                                //Log.i("DESTINO", cleanString( (String) mybutton.getText()));
                                startActivity(ScanningActivity.createIntent(getApplicationContext(), cleanString( (String) mybutton.getText())));
                            else //no es un destino final porque tengo que ir a un segundo nivel
                                startActivity(ListaDestinosDinamicaActivity.createIntent(getApplicationContext(),2,(String) mybutton.getText()));
                        }
                    }
                });
            }
            if (elementos.get(i).equals("")){
                mybutton.setBackgroundColor(Color.WHITE);
                mybutton.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            }

            fila.addView(mybutton);
        }

        tabla.addView(fila);
        filas.add(fila);
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

                    //Comprobar que es un destino valido (puede estar tanto de valor como destino porque da igual el nivel)
                    if(tablaDestinos.containsKey(strSpeech2Text) || tablaDestinos.containsValue(strSpeech2Text)){
                        //llamamos al servidor con ese destino
                        Toast.makeText(getApplicationContext(),
                                strSpeech2Text,
                                Toast.LENGTH_SHORT).show();
                        startActivity(ScanningActivity.createIntent(this, strSpeech2Text));
                    }
                    else{ //Mensaje con destino no valido
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
    public boolean onQueryTextSubmit(String query) {
        String dest = cleanString(query);
        if(tablaDestinos.containsKey(dest) || tablaDestinos.containsValue(dest)){
            //llamamos al servidor con ese destino
            Toast.makeText(getApplicationContext(),
                    dest,
                    Toast.LENGTH_SHORT).show();
            startActivity(ScanningActivity.createIntent(this, dest));
        }
        else{ //Mensaje con destino no valido
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
        ttsManager.shutDown();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Remember to disconnect when finished.
        ttsManager.shutDown();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        nivel--;
        super.onBackPressed();
    }
}
