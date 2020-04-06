package com.example.app_guia_v4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.app_guia_v4.R;

public class ModoDeUsoActivity extends AppCompatActivity {
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ModoDeUsoActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_de_uso);

    }

}
