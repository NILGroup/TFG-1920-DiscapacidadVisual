package com.example.app_guia_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PERMISSIONS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtons();
        checkPermissions();
    }

    //Setting up buttons and listeners.
    private void setupButtons() {

        final Button startSearchingButton = findViewById(R.id.button_start_searching);
        final Button configButton = findViewById(R.id.config_button);
        final Button modo_uso_button = findViewById(R.id.instructions_button);

        startSearchingButton.setOnClickListener(this);
        configButton.setOnClickListener(this);
        modo_uso_button.setOnClickListener(this);
    }
    //Since Android Marshmallow starting a Bluetooth Low Energy scan requires permission from location group.
    private void checkPermissions() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != checkSelfPermissionResult) {
            //Permission not granted so we ask for it. Results are handled in onRequestPermissionsResult() callback.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_CODE_PERMISSIONS == requestCode) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            }
        } else {
            disableButtons();
            Toast.makeText(this, "Location permissions are mandatory to use BLE features on Android 6.0 or higher", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_searching:

                startActivity(ListaDestinosActivity.createIntent(this));
                break;
            case R.id.config_button:
                startActivity(ConfigActivity.createIntent(this));
                break;
            case R.id.instructions_button:
                startActivity(ModoDeUsoActivity.createIntent(this));
                break;
        }
    }

    private void disableButtons() {

    }
}
