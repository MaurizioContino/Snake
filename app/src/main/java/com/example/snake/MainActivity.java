package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Intent intentPlay;
    int HightScore = 0;
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
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        HightScore = prefs.getInt("hightScore", 0); //0 is the default value
        final TextView textViewToChange = (TextView) findViewById(R.id.textView3);
        textViewToChange.setText(Integer.toString(HightScore));
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}