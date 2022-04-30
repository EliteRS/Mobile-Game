package com.example.game;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game.GameView.screenRatioX;
import static com.example.game.GameView.screenRatioY;

public class Player {

    private static double x1,y1;

    boolean isGoingRight = false, isGoingLeft = false;
    int toShoot = 1;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 1, shootCounter = 1;
    Bitmap s1, s2, s3, s4, s5, s6, s7, d1, d2, d3, d4, d5, d6, d7, dead1;
    private GameView gameView;

    Player(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        s1 = BitmapFactory.decodeResource(res, R.drawable.s1);
        s2 = BitmapFactory.decodeResource(res, R.drawable.s2);
        s3 = BitmapFactory.decodeResource(res, R.drawable.s3);
        s4 = BitmapFactory.decodeResource(res, R.drawable.s4);
        s5 = BitmapFactory.decodeResource(res, R.drawable.s5);
        s6 = BitmapFactory.decodeResource(res, R.drawable.s6);
        s7 = BitmapFactory.decodeResource(res, R.drawable.s7);


        width = s1.getWidth();
        height = s1.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        s1 = Bitmap.createScaledBitmap(s1, width, height, false);
        s2 = Bitmap.createScaledBitmap(s2, width, height, false);
        s3 = Bitmap.createScaledBitmap(s3, width, height, false);
        s4 = Bitmap.createScaledBitmap(s4, width, height, false);
        s5 = Bitmap.createScaledBitmap(s5, width, height, false);
        s6 = Bitmap.createScaledBitmap(s6, width, height, false);
        s7 = Bitmap.createScaledBitmap(s7, width, height, false);

        dead1 = BitmapFactory.decodeResource(res, R.drawable.dead1);
        dead1 = Bitmap.createScaledBitmap(dead1, width, height, false);

        y = screenY - 400;
        x = (int) (350 * screenRatioX);  // 64 is the margin from the leftmost part of the screen

    }

    Bitmap getPlayer () {

        if(wingCounter!=0) {
            if (wingCounter == 1) {
                wingCounter++;
                return s1;
            }

            if (wingCounter == 2) {
                wingCounter++;
                return s2;
            }

            if (wingCounter == 3) {
                wingCounter++;
                return s3;
            }

            if (wingCounter == 4) {
                wingCounter++;
                return s4;
            }

            if (wingCounter == 5) {
                wingCounter++;
                return s5;
            }

            if (wingCounter == 6) {
                wingCounter++;
                return s6;
            }

            wingCounter = 1;


            return s7;
        }
        return s7;
    }

    Rect getCollisionShape () {
        return new Rect(x+150, y+200, x + width -150, y + height);
    }

    Bitmap getDead () {
        return dead1;
    }

}
