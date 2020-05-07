package com.example.app_guia_v5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModoDeUsoActivity extends AppCompatActivity implements View.OnClickListener  {
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ModoDeUsoActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_de_uso);

        setupButtons();

    }
    private void setupButtons(){
        Button general_button = (Button) findViewById(R.id.instgeneral_button);
        Button destinos_button = (Button) findViewById(R.id.instdestino_button);
        Button repetir_button = (Button) findViewById(R.id.instrepetir_button);
        Button idetalladas_button = (Button) findViewById(R.id.instdetalladas_button);


        general_button.setOnClickListener(this);
        destinos_button.setOnClickListener(this);
        repetir_button.setOnClickListener(this);
        idetalladas_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.instgeneral_button:
                startActivity(InstruccionesAppActivity.createIntent(this,0));
                break;
            case R.id.instdestino_button:
                startActivity(InstruccionesAppActivity.createIntent(this,1));
                break;
            case R.id.instrepetir_button:
                startActivity(InstruccionesAppActivity.createIntent(this,4));
                break;
            case R.id.instdetalladas_button:
                startActivity(InstruccionesAppActivity.createIntent(this,5));
                break;
        }
    }

}
