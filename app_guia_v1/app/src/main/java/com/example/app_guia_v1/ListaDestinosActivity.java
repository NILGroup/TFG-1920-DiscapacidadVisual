package com.example.app_guia_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;

public class ListaDestinosActivity extends AppCompatActivity implements View.OnClickListener{

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ListaDestinosActivity.class);
    }

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

        aula_13_button.setOnClickListener(this);
        aula_6_button.setOnClickListener(this);
        lab_button.setOnClickListener(this);
        aula_x_button.setOnClickListener(this);
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
