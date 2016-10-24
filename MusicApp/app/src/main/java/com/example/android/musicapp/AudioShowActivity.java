package com.example.android.musicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class AudioShowActivity extends AppCompatActivity {

    private ImageButton playButton, addLibButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_show);
        instantiateButtons();

        playButton.setOnClickListener(new NowPlayingClickListener());
        addLibButton.setOnClickListener(new MainScreenClickListener());
    }

    private void instantiateButtons() {
        playButton = (ImageButton) findViewById(R.id.play_show_button);
        addLibButton = (ImageButton) findViewById(R.id.add_library_button);
    }
}
