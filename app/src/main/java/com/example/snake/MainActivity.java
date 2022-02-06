package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent intentPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void StartPlay(View view) {
        intentPlay = new Intent(this, SnakeActivity.class);
        startActivity(intentPlay);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}