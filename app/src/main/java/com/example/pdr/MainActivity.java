package com.example.pdr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView averageScoreTextView;
    private Button historyButton;
    private Button startTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        averageScoreTextView = findViewById(R.id.averageScoreTextView);
        historyButton = findViewById(R.id.historyButton);
        startTestButton = findViewById(R.id.startTestButton);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        displayAverageScore();
    }

    private void displayAverageScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("TestResults", MODE_PRIVATE);
        int resultCount = sharedPreferences.getInt("resultCount", 0);

        if (resultCount == 0) {
            averageScoreTextView.setText("0%");
            return;
        }

        int totalScore = 0;
        for (int i = 0; i < resultCount; i++) {
            int correctAnswers = sharedPreferences.getInt("correctAnswers" + i, 0);
            int totalQuestions = sharedPreferences.getInt("totalQuestions" + i, 0);
            int score = (correctAnswers * 100) / totalQuestions;
            totalScore += score;
        }

        int averageScore = totalScore / resultCount;
        averageScoreTextView.setText(averageScore + "%");
    }
}
