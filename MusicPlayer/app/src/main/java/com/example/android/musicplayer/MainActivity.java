package com.example.android.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = null;
    private Button playButton, pauseButton, middleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.play_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        middleButton = (Button) findViewById(R.id.middle_button);

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playSong(getApplicationContext());
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSong(getApplicationContext());
            }
        });

        middleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToMiddle(getApplicationContext());
            }
        });

    }

    public void playSong(Context context){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, R.raw.goodnight_fair_lady);
            mediaPlayer.start();
        }else{
            mediaPlayer.start();
        }
    }

    public void pauseSong(Context context){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }else{
            songStartAlert(context);
        }
    }

    public void skipToMiddle(Context context){
        if(mediaPlayer != null){
            int finalTime = mediaPlayer.getDuration();
            mediaPlayer.seekTo(finalTime/2);
        }else{
            songStartAlert(context);
        }
    }

    private void songStartAlert(Context context) {
        Toast toast = Toast.makeText(context, "You should start the song", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
    }
}
