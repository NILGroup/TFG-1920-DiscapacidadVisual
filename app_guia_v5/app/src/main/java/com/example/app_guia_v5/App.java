package com.example.app_guia_v5;

import android.app.Application;
import com.kontakt.sdk.android.common.KontaktSDK;

public class App extends Application {

    private static final String API_KEY = "vVbtLxWwryGBrkYuUQhvDQbedNrOEJqD";

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDependencies();
    }

    //Initializing Kontakt SDK. Insert your API key to allow all samples to work correctly
    private void initializeDependencies() {
        KontaktSDK.initialize(API_KEY);
    }
}
