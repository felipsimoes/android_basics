package com.example.android.musicapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainScreenActivity extends AppCompatActivity {

    private Button b1, b2, b3, b4, b5, b6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        instantiateButtons();

        b1.setOnClickListener(new PodCastClickListener());
        b2.setOnClickListener(new PodCastClickListener());
        b3.setOnClickListener(new PodCastClickListener());
        b4.setOnClickListener(new PodCastClickListener());
        b5.setOnClickListener(new PodCastClickListener());
        b6.setOnClickListener(new PodCastClickListener());
    }

    private void instantiateButtons() {
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
    }

}
