package com.example.app_guia_v5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InstruccionesDeActivity extends AppCompatActivity implements View.OnClickListener {
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, InstruccionesDeActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        setupButtons();

    }
    private void setupButtons(){
        Button anterior_button = (Button) findViewById(R.id.anterior_button);
        Button repro_button = (Button) findViewById(R.id.repetir_button);
        Button siguiente_button = (Button) findViewById(R.id.siguiente_button);

        EditText editText = findViewById(R.id.modo_uso_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        anterior_button.setOnClickListener(this);
        repro_button.setOnClickListener(this);
        siguiente_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.anterior_button:
                break;
            case R.id.repetir_button:
                break;
            case R.id.siguiente_button:
                break;

        }
    }
}
