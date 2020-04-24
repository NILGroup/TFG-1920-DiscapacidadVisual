package com.example.app_guia_v5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Math.abs;

public class InstruccionesDeActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> listaModoUso;
    private static int indice_modoUso;
    private Button anterior_button, repro_button, siguiente_button;
    private EditText editText_modoUso;
    private String inst_PantallaPrincipal = "Instrucciones de la pantalla principal";
    private String inst_BusqDest = "Instrucciones para la búsqueda del destino";
    private String listaDest = "Lista de destinos";
    private String inst_PantallaRuta = "Instrucciones para la pantalla de ruta";
    private String inst_InstDetall = "Instrucciones para instrcciones detalladas";
    private String inst_RepetirInst = "Instrucciones para repetir instruccion";

    public static Intent createIntent(@NonNull Context context, int i) {
        indice_modoUso = i;
        return new Intent(context, InstruccionesDeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        setupButtons();
        setupListaModoUso();
    }
    private void setupButtons(){
        anterior_button = (Button) findViewById(R.id.anterior_button);
        repro_button = (Button) findViewById(R.id.repetir_button);
        siguiente_button = (Button) findViewById(R.id.siguiente_button);

        editText_modoUso = findViewById(R.id.modo_uso_text);
        editText_modoUso.setMovementMethod(new ScrollingMovementMethod());

        anterior_button.setOnClickListener(this);
        repro_button.setOnClickListener(this);
        siguiente_button.setOnClickListener(this);
    }

    private void setupListaModoUso(){
        listaModoUso  = Arrays.asList(inst_PantallaPrincipal,
                inst_BusqDest,
                listaDest,
                inst_PantallaRuta,
                inst_InstDetall,
                inst_RepetirInst);
    }

    private void actualizaModoUso(){
        editText_modoUso.setText(listaModoUso.get(indice_modoUso));
        //llamar al tts
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.anterior_button:
                indice_modoUso = indice_modoUso - 1;
                if(indice_modoUso < 0){
                    indice_modoUso = 5;
                }
                actualizaModoUso();
                break;
            case R.id.repetir_button:
                actualizaModoUso();
                break;
            case R.id.siguiente_button:
                indice_modoUso = (indice_modoUso + 1)%6;
                actualizaModoUso();
                break;
        }
    }
}
