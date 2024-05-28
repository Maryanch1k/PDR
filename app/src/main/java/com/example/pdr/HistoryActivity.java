package com.example.pdr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private Button backHomeButton;
    private LinearLayout historyLayout;
    private List<TestResult> testResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyLayout = findViewById(R.id.historyLayout);
        backHomeButton = findViewById(R.id.backToHomeButton);

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Загрузка результатов из SharedPreferences
        loadResults();

        // Отображение результатов
        displayResults();
    }

    private void loadResults() {
        SharedPreferences sharedPreferences = getSharedPreferences("TestResults", MODE_PRIVATE);
        int resultCount = sharedPreferences.getInt("resultCount", 0);
        testResults = new ArrayList<>();

        for (int i = 0; i < resultCount; i++) {
            int correctAnswers = sharedPreferences.getInt("correctAnswers" + i, 0);
            int totalQuestions = sharedPreferences.getInt("totalQuestions" + i, 0);
            int score = (correctAnswers * 100) / totalQuestions;
            String time = sharedPreferences.getString("time" + i, "");
            String date = sharedPreferences.getString("date" + i, "");
            testResults.add(new TestResult(score, correctAnswers, totalQuestions, time, date));
        }

        // Сохранение только последних 5 результатов
        if (testResults.size() > 4) {
            testResults = testResults.subList(testResults.size() - 4, testResults.size());
        }
    }

    private void displayResults() {
        for (TestResult result : testResults) {
            View resultView = getLayoutInflater().inflate(R.layout.history_result_entry, null);

            TextView timeTextView = resultView.findViewById(R.id.timeTextView);
            TextView dateTextView = resultView.findViewById(R.id.dateTextView);
            TextView scoreTextView = resultView.findViewById(R.id.scoreTextView);
            TextView correctAnswersTextView = resultView.findViewById(R.id.correctAnswersTextView);

            timeTextView.setText(result.getTime());
            dateTextView.setText(result.getDate());
            scoreTextView.setText(result.getScore() + "%");
            correctAnswersTextView.setText("Відповіли правильно: " + result.getCorrectAnswers() + " з " + result.getTotalQuestions());

            if (result.getScore() < 87) {
                scoreTextView.setBackgroundResource(R.drawable.circle_background_red);
            } else {
                scoreTextView.setBackgroundResource(R.drawable.circle_background);
            }

            historyLayout.addView(resultView);
        }
    }

    private static class TestResult {
        private final int score;
        private final int correctAnswers;
        private final int totalQuestions;
        private final String time;
        private final String date;

        public TestResult(int score, int correctAnswers, int totalQuestions, String time, String date) {
            this.score = score;
            this.correctAnswers = correctAnswers;
            this.totalQuestions = totalQuestions;
            this.time = time;
            this.date = date;
        }

        public int getScore() {
            return score;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public String getTime() {
            return time;
        }

        public String getDate() {
            return date;
        }
    }
}
