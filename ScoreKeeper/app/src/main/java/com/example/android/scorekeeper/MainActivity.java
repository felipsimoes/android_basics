package com.example.android.scorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int scoreTeamA = 0;
    private int scoreTeamB = 0;

    private TextView scoreTeamAView;
    private TextView scoreTeamBView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTeamAView = (TextView) findViewById(R.id.team_a_score);
        scoreTeamBView = (TextView) findViewById(R.id.team_b_score);

        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }

    public void displayForTeamA(int score) {
        scoreTeamAView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score) {
        scoreTeamBView.setText(String.valueOf(score));
    }

    public static void updateCounter(TextView textView, int score){
        textView.setText(String.valueOf(score));
    }

    public void onSubmit(View view){
        switch (view.getId()){
            case R.id.button_A6:
                scoreTeamA+=6;
                updateCounter(scoreTeamAView,scoreTeamA);
                break;
            case R.id.button_A3:
                scoreTeamA+=3;
                updateCounter(scoreTeamAView,scoreTeamA);
                break;
            case R.id.button_A2:
                scoreTeamA+=2;
                updateCounter(scoreTeamAView,scoreTeamA);
                break;
            case R.id.button_A1:
                scoreTeamA++;
                updateCounter(scoreTeamAView,scoreTeamA);
                break;
            case R.id.button_B6:
                scoreTeamB+=6;
                updateCounter(scoreTeamBView,scoreTeamB);
                break;
            case R.id.button_B3:
                scoreTeamB+=3;
                updateCounter(scoreTeamBView,scoreTeamB);
                break;
            case R.id.button_B2:
                scoreTeamB+=2;
                updateCounter(scoreTeamBView,scoreTeamB);
                break;
            case R.id.button_B1:
                scoreTeamB++;
                updateCounter(scoreTeamBView,scoreTeamB);
                break;
        }
    }

    public void resetScores(View view){
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }
}
