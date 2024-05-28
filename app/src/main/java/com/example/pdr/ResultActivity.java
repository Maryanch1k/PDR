package com.example.pdr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class ResultActivity extends AppCompatActivity {

    private TextView percentageTextView;
    private TextView resultDetailsTextView;
    private LottieAnimationView backgroundAnimation;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backgroundAnimation = findViewById(R.id.backgroundAnimation);
        backgroundAnimation.setAnimation(R.raw.results);
        backgroundAnimation.playAnimation();

        percentageTextView = findViewById(R.id.percentageTextView);
        resultDetailsTextView = findViewById(R.id.resultDetailsTextView);
        Button retryButton = findViewById(R.id.retryButton);
        homeButton = findViewById(R.id.homeButton);

        // Получение результатов из интента
        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("correctAnswers", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 8);
        int passingScore = intent.getIntExtra("passingScore", 87);

        int percentage = (correctAnswers * 100) / totalQuestions;
        percentageTextView.setText(percentage + "%");

        if (percentage >= 87) {
            percentageTextView.setBackgroundResource(R.drawable.circle_background);
        } else {
            percentageTextView.setBackgroundResource(R.drawable.circle_background_red);
        }

        String resultDetails = "ВІДПОВІЛИ ПРАВИЛЬНО: " + correctAnswers + " ІЗ " + totalQuestions + "\nПРОХІДНИЙ БАЛ: " + passingScore + "%";
        resultDetailsTextView.setText(resultDetails);

        retryButton.setOnClickListener(v -> {
            Intent retryIntent = new Intent(ResultActivity.this, TestActivity.class);
            startActivity(retryIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        // Сохранение результата в SharedPreferences
        saveResult(correctAnswers, totalQuestions, percentage);
    }

    private void saveResult(int correctAnswers, int totalQuestions, int score) {
        SharedPreferences sharedPreferences = getSharedPreferences("TestResults", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int resultCount = sharedPreferences.getInt("resultCount", 0);
        resultCount++;

        // Если результатов больше 5, удаляем самый старый
        if (resultCount > 5) {
            for (int i = 1; i < resultCount; i++) {
                editor.putInt("correctAnswers" + (i - 1), sharedPreferences.getInt("correctAnswers" + i, 0));
                editor.putInt("totalQuestions" + (i - 1), sharedPreferences.getInt("totalQuestions" + i, 0));
                editor.putString("time" + (i - 1), sharedPreferences.getString("time" + i, ""));
                editor.putString("date" + (i - 1), sharedPreferences.getString("date" + i, ""));
            }
            resultCount = 5;
        }

        editor.putInt("correctAnswers" + (resultCount - 1), correctAnswers);
        editor.putInt("totalQuestions" + (resultCount - 1), totalQuestions);
        editor.putString("time" + (resultCount - 1), java.text.DateFormat.getTimeInstance().format(new java.util.Date()));
        editor.putString("date" + (resultCount - 1), java.text.DateFormat.getDateInstance().format(new java.util.Date()));
        editor.putInt("resultCount", resultCount);

        editor.apply();
    }
}
