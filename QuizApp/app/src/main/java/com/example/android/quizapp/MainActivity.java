package com.example.android.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int score;
    private RadioGroup answerQOne, answerQFive;
    private TextView answerQTwo, answerQThree, answerQFour, answerQSeven;
    private CheckBox answerCheckBoxAnswerOne, answerCheckBoxAnswerTwo,
            answerCheckBoxAnswerThree, answerCheckBoxAnswerFour;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateViews();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countScore();
                Toast toast;
                if(score == 7){
                    toast = Toast.makeText(getApplicationContext(),
                            "You are awesome! Maximum score", Toast.LENGTH_LONG);
                }
                else if (score!=0){
                    toast = Toast.makeText(getApplicationContext(),
                            "Your score is :"+score, Toast.LENGTH_LONG);
                }else{
                    toast = Toast.makeText(getApplicationContext(),
                            "Try again, you don't have a score yet", Toast.LENGTH_LONG);
                }
                toast.show();
                score = 0;
            }
        });
    }

    private void countScore() {
        scoreQOne();
        scoreQTwo();
        scoreQThree();
        scoreQFour();
        scoreQFive();
        scoreQSix();
        scoreQSeven();
    }

    private void scoreQOne() {
        int id = answerQOne.getCheckedRadioButtonId();
        if(id != -1 && id == R.id.q1opt3_radio_button){
            score++;
        }
    }

    private void scoreQTwo() {
        String text = answerQTwo.getText().toString().trim().toLowerCase();
        if(text.contains("14") || text.contains("fourteen")){
            score++;
        }
    }

    private void scoreQThree() {
        String text = answerQThree.getText().toString().trim().toLowerCase();
        if(text.contains("shakespeare")){
            score++;
        }
    }

    private void scoreQFour() {
        String text = answerQFour.getText().toString().trim().toLowerCase();
        if(text.contains("peter pan")){
            score++;
        }
    }

    private void scoreQFive() {
        int id = answerQFive.getCheckedRadioButtonId();
        if(id != -1 && id == R.id.q5opt1_radio_button){
            score++;
        }
    }

    private void scoreQSix() {
        boolean one, two, three, four;
        one = answerCheckBoxAnswerOne.isChecked();
        two = answerCheckBoxAnswerTwo.isChecked();
        three = answerCheckBoxAnswerThree.isChecked();
        four = answerCheckBoxAnswerFour.isChecked();
        if(one && two && three && !four){
            score++;
        }
    }

    private void scoreQSeven() {
        String text = answerQSeven.getText().toString().trim().toLowerCase();
        if(text.contains("hobbit")){
            score++;
        }
    }

    private void instantiateViews(){

        answerQOne = (RadioGroup) findViewById(R.id.radio_group_one);
        answerQFive = (RadioGroup) findViewById(R.id.radio_group_five);

        answerQTwo = (TextView) findViewById(R.id.answerTwo);
        answerQThree = (TextView) findViewById(R.id.answerThree);
        answerQFour = (TextView) findViewById(R.id.answerFour);
        answerQSeven = (TextView) findViewById(R.id.answerSeven);

        answerCheckBoxAnswerOne = (CheckBox) findViewById(R.id.checkboxOpt1);
        answerCheckBoxAnswerTwo = (CheckBox) findViewById(R.id.checkboxOpt2);
        answerCheckBoxAnswerThree = (CheckBox) findViewById(R.id.checkboxOpt3);
        answerCheckBoxAnswerFour = (CheckBox) findViewById(R.id.checkboxOpt4);

        submitButton = (Button) findViewById(R.id.button_submit);
    }
}
