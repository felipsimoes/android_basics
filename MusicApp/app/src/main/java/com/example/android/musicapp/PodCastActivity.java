package com.example.android.musicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PodCastActivity extends AppCompatActivity {

    private Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        instantiateButtons();

        b1.setOnClickListener(new AudioShowClickListener());
        b2.setOnClickListener(new AudioShowClickListener());
        b3.setOnClickListener(new AudioShowClickListener());
    }

    private void instantiateButtons() {
        b1 = (Button) findViewById(R.id.bshow1);
        b2 = (Button) findViewById(R.id.bshow2);
        b3 = (Button) findViewById(R.id.bshow3);
    }

}
