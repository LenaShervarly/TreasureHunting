package com.home.croaton.followme.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.database.RemoteDatabaseRespresenter;

import java.util.ArrayList;

public class Quizz4answersActivity extends AppCompatActivity {

    private int points;
    private RemoteDatabaseRespresenter dbRepresenter;
    private String correctAnswer;

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
        Cursor allContent = dbRepresenter.getAllData();


        if(allContent.getCount() == 0) {
            return;
        }
        //int rand = 1;

        while (allContent.moveToNext()) {
            if(allContent.getString(7).contains("0")) {
                question.setText(allContent.getString(1));
                answer1.setText(allContent.getString(2));
                answer2.setText(allContent.getString(3));
                answer3.setText(allContent.getString(4));
                answer4.setText(allContent.getString(5));

                correctAnswer = allContent.getString(2);

                dbRepresenter.updatePassed(allContent.getString(0));
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
        button.setBackgroundColor(getResources().getColor(R.color.orange_main));
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
