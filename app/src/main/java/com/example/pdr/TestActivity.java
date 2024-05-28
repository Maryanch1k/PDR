package com.example.pdr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TextView questionTextView;
    private CheckBox answerCheckBox1;
    private CheckBox answerCheckBox2;
    private CheckBox answerCheckBox3;
    private Button nextButton;
    private Button backButton;
    private GridLayout questionNumbersLayout;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;

    private List<Question> selectedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        questionTextView = findViewById(R.id.questionTextView);
        answerCheckBox1 = findViewById(R.id.answerCheckBox1);
        answerCheckBox2 = findViewById(R.id.answerCheckBox2);
        answerCheckBox3 = findViewById(R.id.answerCheckBox3);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        questionNumbersLayout = findViewById(R.id.questionNumbersLayout);

        // Инициализация и предварительная загрузка анимации
        LottieAnimationView preloadedAnimation = new LottieAnimationView(this);
        preloadedAnimation.setAnimation(R.raw.results);
        preloadedAnimation.setRepeatCount(LottieDrawable.INFINITE);
        preloadedAnimation.playAnimation();

        // Выбираем случайный набор вопросов
        List<List<Question>> questionSets = QuestionSet.getQuestionSets();
        selectedQuestions = questionSets.get((int) (Math.random() * questionSets.size()));

        // Создаем номера вопросов
        createQuestionNumbers();

        // Обновляем вопрос и подсветку
        updateQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (currentQuestionIndex < selectedQuestions.size() - 1) {
                    currentQuestionIndex++;
                } else {
                    showResult();
                }
                updateQuestion();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                }
                updateQuestion();
            }
        });
    }

    private void createQuestionNumbers() {
        for (int i = 0; i < selectedQuestions.size(); i++) {
            final int index = i;
            TextView textView = new TextView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            textView.setLayoutParams(params);
            textView.setText(String.valueOf(i + 1));
            textView.setTextSize(18);
            textView.setPadding(8, 8, 8, 8);
            textView.setBackgroundResource(R.drawable.number_background);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuestionIndex = index;
                    updateQuestion();
                }
            });
            questionNumbersLayout.addView(textView);
        }
    }

    private void updateQuestion() {
        if (currentQuestionIndex == selectedQuestions.size() - 1) {
            nextButton.setText("Завершити");
        } else {
            nextButton.setText("Далі");
        }

        Question question = selectedQuestions.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestion());
        answerCheckBox1.setText(question.getOption1());
        answerCheckBox2.setText(question.getOption2());
        answerCheckBox3.setText(question.getOption3());
        answerCheckBox1.setChecked(false);
        answerCheckBox2.setChecked(false);
        answerCheckBox3.setChecked(false);

        highlightCurrentQuestion();
    }

    private void highlightCurrentQuestion() {
        for (int i = 0; i < questionNumbersLayout.getChildCount(); i++) {
            TextView textView = (TextView) questionNumbersLayout.getChildAt(i);
            if (i == currentQuestionIndex) {
                textView.setBackgroundResource(R.drawable.selected_question_number_background);
            } else {
                textView.setBackgroundResource(R.drawable.number_background);
            }
        }
    }

    private void checkAnswer() {
        Question question = selectedQuestions.get(currentQuestionIndex);
        String selectedAnswer = "";
        if (answerCheckBox1.isChecked()) {
            selectedAnswer = answerCheckBox1.getText().toString();
        } else if (answerCheckBox2.isChecked()) {
            selectedAnswer = answerCheckBox2.getText().toString();
        } else if (answerCheckBox3.isChecked()) {
            selectedAnswer = answerCheckBox3.getText().toString();
        }

        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            correctAnswers++;
        } else {
            wrongAnswers++;
        }
    }

    private void showResult() {
        Intent intent = new Intent(TestActivity.this, ResultActivity.class);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("totalQuestions", selectedQuestions.size());
        startActivity(intent);
        finish();
    }
}
