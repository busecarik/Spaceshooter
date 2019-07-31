package com.busecarik.spaceshooter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        //borrowing code
        final Typeface _typeface = Typeface.createFromAsset(getAssets(), "fonts/theme.ttf");

        final TextView highScore = findViewById(R.id.highscore_text);
        highScore.setTypeface(_typeface);

        SharedPreferences prefs = getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
        int longestDistance = prefs.getInt(Config.LONGEST_DIST, 0);
        highScore.setTextSize(Config.TEXT_SIZE);
        highScore.setText(getString(R.string.highScoreText, longestDistance));
    }

    @Override
    public void onClick(View v) {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
}
