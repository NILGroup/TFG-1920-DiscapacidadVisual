package com.example.app_guia_v5;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

    private String[] listaModoUso;
    private static int indice_modoUso;
    private Button anterior_button, repro_button, siguiente_button;
    private EditText editText_modoUso;

    private TTSManager ttsManager = null;

    public static Intent createIntent(@NonNull Context context, int i) {
        indice_modoUso = i;
        return new Intent(context, InstruccionesDeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        //Inicializamos el objeto de la clase TTSManager
        ttsManager = new TTSManager();
        ttsManager.init(this);

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
        Resources res = getResources();
        listaModoUso  = res.getStringArray(R.array.instrucciones_array);
    }

    private void actualizaModoUso(){
        editText_modoUso.setText(listaModoUso[indice_modoUso]);
        //Reproducimos las instrucciones:
        ttsManager.addQueue(listaModoUso[indice_modoUso]);

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
