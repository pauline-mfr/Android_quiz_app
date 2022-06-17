package com.mfr.europequiz;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mfr.europequiz.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private FragmentQuizBinding binding;

    //QUIZ VARIABLES
    TextView totalQuestionsText;
    TextView questionToDisplay;
    Button answer1, answer2, answer3;
    Button submitAnswer;

    int score = 0;
    int totalQuestions = QuizContent.question.length;
    int currentQuestion = 0;
    String selectedAnswer = "";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //QUIZ ELEMENTS
        this.totalQuestionsText = (TextView) view.findViewById(R.id.total_questions);
        this.questionToDisplay = (TextView) view.findViewById(R.id.question);
        this.answer1 = (Button) view.findViewById(R.id.answer1);
        this.answer2 = (Button) view.findViewById(R.id.answer2);
        this.answer3 = (Button) view.findViewById(R.id.answer3);
        this.submitAnswer = (Button) view.findViewById(R.id.submit_btn);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        submitAnswer.setOnClickListener(this);

        //SET QUESTION
        totalQuestionsText.setText("Total questions: " + this.totalQuestions);
        loadNextQuestion();


        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(QuizFragment.this)
                        .navigate(R.id.action_QuizFragment_to_StartQuizFragment);
            }
        });
    }

    //QUIZ FUNCTIONS
    public void loadNextQuestion() {

        if(this.currentQuestion == this.totalQuestions) {
            this.endOfQuiz();
            return; //so it won't load another question
        }

        this.questionToDisplay.setText(QuizContent.question[currentQuestion]);
        this.answer1.setText(QuizContent.choices[currentQuestion][0]);
        this.answer2.setText(QuizContent.choices[currentQuestion][1]);
        this.answer3.setText(QuizContent.choices[currentQuestion][2]);
    }

    public void endOfQuiz() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Your results")
                .setMessage("Score = " +score+ " / " +totalQuestions)
                .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    public void restartQuiz() {
        this.score = 0;
        this.currentQuestion = 0;
        this.loadNextQuestion();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //WHEN USER CLICKS
    @Override
    public void onClick(View view) {
        answer1.setBackgroundColor(Color.WHITE);
        answer2.setBackgroundColor(Color.WHITE);
        answer3.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;

        if(clickedButton.getId() == R.id.submit_btn) {

            //INCREASE SCORE
            if(this.selectedAnswer.equals(QuizContent.correctAnswers[this.currentQuestion])) { //if correct answer
                this.score++;
            }

            this.currentQuestion++;
            this.loadNextQuestion();

        } else {
            //one of choices btn clicked
            this.selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);
        }
    }
}