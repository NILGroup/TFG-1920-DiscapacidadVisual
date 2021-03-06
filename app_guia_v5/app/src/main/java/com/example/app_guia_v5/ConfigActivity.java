package com.example.app_guia_v5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Switch;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ConfigActivity.class);
    }

    private static Switch modo_verb_switch = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);
        setupButtons();

    }

    private void setupButtons(){

        Button volume_button = (Button) findViewById(R.id.volumen_button);
        modo_verb_switch = (Switch) findViewById(R.id.modo_verb_switch);

        volume_button.setOnClickListener(this);
        modo_verb_switch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) { //switches
        switch (view.getId()) {
            case R.id.volumen_button:

                break;
            case R.id.modo_verb_switch:
                ScanningActivity.setVerbose(modo_verb_switch.isChecked());
                break;
        }
    }
    public static Switch getModo_verb_switch() {
        return modo_verb_switch;
    }

}
