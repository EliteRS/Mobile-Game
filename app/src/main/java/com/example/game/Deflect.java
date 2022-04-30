package com.example.game;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game.GameView.screenRatioX;
import static com.example.game.GameView.screenRatioY;

public class Deflect {

    private static double x1,y1;

    boolean isGoingRight = false, isGoingLeft = false, isGoingDeflect = false;
    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 1, shootCounter = 1;
    Bitmap s1, s2, s3, s4, s5, s6, s7, d1, d2, d3, d4, d5, d6, d7, dead1;
    private GameView gameView;

    Deflect(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        d1 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d2 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d3 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d4 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d5 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d6 = BitmapFactory.decodeResource(res, R.drawable.shield);
        d7 = BitmapFactory.decodeResource(res, R.drawable.shield);

        width = d1.getWidth();
        height = d1.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);


        d1 = Bitmap.createScaledBitmap(d1, width, height, false);
        d2 = Bitmap.createScaledBitmap(d2, width, height, false);
        d3 = Bitmap.createScaledBitmap(d3, width, height, false);
        d4 = Bitmap.createScaledBitmap(d4, width, height, false);
        d5 = Bitmap.createScaledBitmap(d5, width, height, false);
        d6 = Bitmap.createScaledBitmap(d6, width, height, false);
        d7 = Bitmap.createScaledBitmap(d7, width, height, false);


        y = screenY - 400;
        x = (int) (350 * screenRatioX);

    }

    Bitmap getDeflect () {

                if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return d1;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return d2;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return d3;
            }

            if (shootCounter == 4) {
                shootCounter++;
                return d4;
            }

            if (shootCounter == 5) {
                shootCounter++;
                return d5;
            }

            if (shootCounter == 6) {
                shootCounter++;
                return d6;
            }


            shootCounter = 1;
            toShoot--;
            gameView.newSound();

            return d7;
        }


        return d7;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }


}
