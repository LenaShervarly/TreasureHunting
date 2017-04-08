package com.home.jsquad.knowhunt.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.android.database.RemoteDatabaseRespresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Quizz4answersActivity extends AppCompatActivity {

    private int points;
    private RemoteDatabaseRespresenter dbRepresenter;
    private String correctAnswer;
    private Random rand;
    private List<String> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz4answers);

        setContent();
    }

    private void setContent(){
        TextView question = (TextView) findViewById(R.id.textView);
        Button answer1 = (Button)findViewById(R.id.guessMelody_option1);
        Button answer2 = (Button)findViewById(R.id.guessMelody_option2);
        Button answer3 = (Button)findViewById(R.id.guessMelody_option3);
        Button answer4 = (Button)findViewById(R.id.guessMelody_option4);

        dbRepresenter = new RemoteDatabaseRespresenter(this);
        dbRepresenter.getDataFromServer(this);
        Cursor allContent = dbRepresenter.getAllData();


        if(allContent.getCount() == 0) {
            return;
        }

        while (allContent.moveToNext()) {
            if(allContent.getString(6).contains("0")) {
                question.setText(allContent.getString(1));

                answers = new ArrayList<>();
                for(int i = 2; i <=5; i++) {
                    answers.add(allContent.getString(i));
                }
                Collections.shuffle(answers);

                answer1.setText(answers.get(0));
                answer2.setText(answers.get(1));
                answer3.setText(answers.get(2));
                answer4.setText(answers.get(3));

                correctAnswer = allContent.getString(2);

                dbRepresenter.updatePassedQA(allContent.getString(0));
                allContent.close();
                return;
            }
            else
                continue;
        }
        if(!allContent.moveToNext()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(true);
            alert.setTitle("Already solved!");
            alert.setMessage("You've already solved this task. Please, try some other spots!");
            alert.show();
        }
    }

    public void onTryingAnswer(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();

        if( buttonText.equals(correctAnswer))
            points = 5;
        else
            points = 0;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        returnToMapActivity();
    }

    public void returnToMapActivity() {
        Intent score = new Intent();
        if(points > 0)
            score.putExtra("message", "Well done! You've got " + points +" points");
        else
            score.putExtra("message", "Nice try! Unfortunately you haven't got point for this round");
        score.putExtra("scores", points);
        setResult(Activity.RESULT_OK, score);
        finish();
    }
}
