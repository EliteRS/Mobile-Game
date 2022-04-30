package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer playTheme;
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        findViewById(R.id.playbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HIGH SCORE: " + prefs.getInt("highscore", 0));
        isMute = prefs.getBoolean("isMute", false);

        //theme tune
        playTheme = MediaPlayer.create(MainActivity.this, R.raw.theme);
        playTheme.start();

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute) {
            volumeCtrl.setImageResource(R.drawable.sound_off);
            playTheme.stop();
        }else {
            volumeCtrl.setImageResource(R.drawable.sound_on);
            playTheme.start();
        }

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute) {
                    volumeCtrl.setImageResource(R.drawable.sound_off);
                    playTheme.stop();
                }else {
                    volumeCtrl.setImageResource(R.drawable.sound_on);
                    playTheme.start();
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();

            }
        });

    }
}
