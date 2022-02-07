package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class SnakeActivity extends AppCompatActivity {
    SnakeEngine snakeEngine;

    Intent intentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        snakeEngine=new SnakeEngine(this, size);
        setContentView(snakeEngine);

        intentMain = new Intent(this, MainActivity.class);
    }

        @Override
        protected void onStart() {
            super.onStart();
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            snakeEngine.highscore = prefs.getInt("hightScore", 0); //0 is the default value


            snakeEngine.resume();
        }

    public void SaveHighScore(int score) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("hightScore", score);
        editor.commit();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        snakeEngine.highscore = savedInstanceState.getInt("highscore");
        snakeEngine.score = savedInstanceState.getInt("score");
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("highscore", snakeEngine.highscore);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}