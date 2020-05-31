package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    final int NUM_OF_WORDS = 1000;

    String[] englishWords;
    String[] nonEnglishWords;

    String word;

    TextView mainWordTextView;
    TextView numCorrectTextView;
    TextView backTextView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;

    Button correctButton;

    Boolean languageSelected = false;
    Boolean startQuiz = false;

    Set<String> wordsUsed = new HashSet<>();

    int mainWordIndex;

    int numWordsCorrect = 0;
    int totalWords = 0;

    public String generateNewWord(boolean useEnglish, Set<String> setOfWords, boolean isMainWord) {

        int min = 0;
        int max = NUM_OF_WORDS - 1;
        int random = new Random().nextInt((max - min) + 1) + min;

        if (useEnglish) {
            word = englishWords[random];

            //Find different word if word was already used
            while (setOfWords.contains(word)) {
                random = new Random().nextInt((max - min) + 1) + min;
                word = englishWords[random];
            }
        }
        else {
            word = nonEnglishWords[random];

            //Find different word if word was already used
            while (setOfWords.contains(word)) {
                random = new Random().nextInt((max - min) + 1) + min;
                word = nonEnglishWords[random];
            }
        }

        if (isMainWord) {
            mainWordIndex = random;
        }
        setOfWords.add(word);
        return word;
    }

    public void backButton(View view) {
        if (startQuiz == true) {
            startQuiz = false;
            languageSelected = false;
            numCorrectTextView.setText("START");
            backTextView.setVisibility(View.INVISIBLE);
            mainWordTextView.setText("Select Language");

            button4.setVisibility(View.VISIBLE);
            button5.setVisibility(View.VISIBLE);

            button0.setText("Spanish");
            button1.setText("Italian");
            button2.setText("German");
            button3.setText("French");
            button4.setText("Russian");
            button5.setText("Japanese");

        }
    }

    public void startButton(View view) {
        //no language selected in home screen
        if (startQuiz == false && languageSelected == false) {
            Toast.makeText(this, "Please select a language", Toast.LENGTH_LONG).show();
        }
        //language selected in home screen
        //start quiz
        else if (startQuiz == false && languageSelected == true) {

            numCorrectTextView.setText("0/0");
            backTextView.setVisibility(View.VISIBLE);
            backTextView.setText("BACK");
            startQuiz = true;
            start();
            wordsUsed.clear();
            totalWords = 0;
            numWordsCorrect = 0;
        }
    }

    public void start() {

        button4.setVisibility(View.INVISIBLE);
        button5.setVisibility(View.INVISIBLE);

        final boolean isMainWord = true;
        final boolean isNotMainWord = false;

        //1 = word is English, 0 = word is not English
        int useEnglishInt = new Random().nextInt(2);
        boolean useEnglish; //Main word is English if true

        if (useEnglishInt == 1) {
            useEnglish = true;
        } else {
            useEnglish = false;
        }

        //Generate main word
        String mainWord = generateNewWord(useEnglish, wordsUsed, isMainWord);
        mainWordTextView.setText(mainWord);

        String buttonTextAnswer;
        //set answer as an option
        if (useEnglish) {
            buttonTextAnswer = nonEnglishWords[mainWordIndex];
        } else {
            buttonTextAnswer = englishWords[mainWordIndex];
        }

        //Generate other words
        Set<String> otherWordsUsed = new HashSet<>(); //create set so there are no duplicates
        otherWordsUsed.add(buttonTextAnswer); //add main word answer so it is not an option for selection

        String buttonText0 = generateNewWord(!useEnglish, otherWordsUsed, isNotMainWord);
        String buttonText1 = generateNewWord(!useEnglish, otherWordsUsed, isNotMainWord);
        String buttonText2 = generateNewWord(!useEnglish, otherWordsUsed, isNotMainWord);

        //Randomize which button contains the answer
        int buttonAnswer = new Random().nextInt(4);

        switch (buttonAnswer) {

            case 0:
                button0.setText(buttonTextAnswer);
                correctButton = button0;
                button1.setText(buttonText0);
                button2.setText(buttonText1);
                button3.setText(buttonText2);
                break;
            case 1:
                button0.setText(buttonText0);
                button1.setText(buttonTextAnswer);
                correctButton = button1;
                button2.setText(buttonText1);
                button3.setText(buttonText2);
                break;
            case 2:
                button0.setText(buttonText0);
                button1.setText(buttonText1);
                button2.setText(buttonTextAnswer);
                correctButton = button2;
                button3.setText(buttonText2);
                break;
            case 3:
                button0.setText(buttonText0);
                button1.setText(buttonText1);
                button2.setText(buttonText2);
                button3.setText(buttonTextAnswer);
                correctButton = button3;
                break;
        }
    }

    public void checkAnswer(View view) {

        final boolean ANSWER_CORRECT = true;
        final boolean ANSWER_INCORRECT = false;

        Button buttonSelected = (Button) view;

        if (buttonSelected == correctButton) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();
                setTotal(ANSWER_CORRECT);

        }
        else {
                Toast.makeText(this, "Incorrect. Answer: " + correctButton.getText().toString(), Toast.LENGTH_LONG).show();
                setTotal(ANSWER_INCORRECT);
        }

    }

    public void setTotal(boolean answerCorrect) {

            if (answerCorrect) {
                numWordsCorrect += 1;
            }

            totalWords += 1;

            numCorrectTextView.setText(numWordsCorrect + "/" + totalWords);

    }

    public void setSpanish(View view) {

        if (startQuiz == true) {
            checkAnswer(view);
            start();
        }
        else {
            nonEnglishWords = getResources().getStringArray(R.array.spanish_words);
            mainWordTextView.setText("Spanish selected");
            languageSelected = true;
        }


    }

    public void setItalian(View view) {

        if (startQuiz == true) {
            checkAnswer(view);
            start();
        }
        else {
            nonEnglishWords = getResources().getStringArray(R.array.italian_words);
            mainWordTextView.setText("Italian selected");
            languageSelected = true;
        }
    }

    public void setGerman(View view) {

        if (startQuiz == true) {
            checkAnswer(view);
            start();
        }
        else {
            nonEnglishWords = getResources().getStringArray(R.array.german_words);
            mainWordTextView.setText("German selected");
            languageSelected = true;
        }
    }

    public void setFrench(View view) {

        if (startQuiz == true) {
            checkAnswer(view);
            start();
        }
        else {
            nonEnglishWords = getResources().getStringArray(R.array.french_words);
            mainWordTextView.setText("French selected");
            languageSelected = true;
        }
    }

    public void setRussian(View view) {
        nonEnglishWords = getResources().getStringArray(R.array.russian_words);
        mainWordTextView.setText("Russian selected");
        languageSelected = true;
    }

    public void setJapanese(View view) {
        nonEnglishWords = getResources().getStringArray(R.array.japanese_words);
        mainWordTextView.setText("Japanese selected");
        languageSelected = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        englishWords = getResources().getStringArray(R.array.english_words);

        mainWordTextView = findViewById(R.id.wordTextView);
        numCorrectTextView = findViewById(R.id.numCorrectTextView);
        backTextView = findViewById(R.id.backTextView);

        mainWordTextView.setText("Select Language");
        backTextView.setVisibility(View.INVISIBLE);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);

        button0.setText("Spanish");
        button1.setText("Italian");
        button2.setText("German");
        button3.setText("French");
        button4.setText("Russian");
        button5.setText("Japanese");


    }
}
