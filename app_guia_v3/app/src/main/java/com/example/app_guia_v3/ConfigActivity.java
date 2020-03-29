package com.example.app_guia_v3;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class ConfigActivity {
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ConfigActivity.class);
    }
}
