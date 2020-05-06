package com.example.app_guia_v5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ListaDestinosDinamicaActivity extends AppCompatActivity implements View.OnClickListener {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosDinamicaActivity.class);
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        tabla = (TableLayout) findViewById(R.id.tabla);
        filas=new ArrayList<TableRow>();
        FILAS=COLUMNAS=0;
        setupButtons();


    }

    private void setupButtons(){

        for(int i = 0; i < 15; i++)
        {
            ArrayList<String> elementos = new ArrayList<String>();
            elementos.add(Integer.toString(i));
            elementos.add("Casilla [" + i + ", 0]");
            elementos.add("Casilla [" + i + ", 1]");
            elementos.add("Casilla [" + i + ", 2]");
            elementos.add("Casilla [" + i + ", 3]");
            agregarFilaTabla(elementos);
        }

    }

    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    public void agregarFilaTabla(ArrayList<String> elementos)
    {
        TableRow.LayoutParams layoutCelda = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            Button mybutton = new Button(this);
            mybutton.setLayoutParams(layoutCelda);
            mybutton.setText("Boton "+ i);
            //texto.setText(String.valueOf(elementos.get(i))); para luego coger el texto de los botones
            //button.setGravity(Gravity.CENTER_HORIZONTAL);

            fila.addView(mybutton);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.volumen_button:

                break;
            case R.id.modo_verb_switch:
                ScanningActivity.setVerbose(modo_verb_switch.isChecked());
                break;*/
        }
    }


}
