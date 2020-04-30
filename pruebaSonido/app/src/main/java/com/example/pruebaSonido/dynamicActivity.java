package com.example.pruebaSonido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.LinearLayout;

public class dynamicActivity extends AppCompatActivity {


    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, dynamicActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        setupButtons();
    }


    private void setupButtons() {
        Button myButton = new Button(this);
        myButton.setText("Push Me");

        LinearLayout ll = (LinearLayout)findViewById(R.id.my_ll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);
    }
}
