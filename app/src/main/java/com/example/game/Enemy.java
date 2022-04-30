package com.example.game;
import android.graphics.Rect;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game.GameView.screenRatioX;
import static com.example.game.GameView.screenRatioY;

public class Enemy {
    public int speed = 20; //move right to left 20 pixels faster or slower
    public boolean wasShot = true;
    public int toShoot;
    int x=500, y, width, height, enemyCounter = 1;
    Bitmap enemy1, enemy2, enemy3, enemy4, enemy5, enemy6, enemy7;
    private GameView gameView;
    Enemy(Resources res) {
        this.gameView = gameView;

        enemy1 = BitmapFactory.decodeResource(res, R.drawable.enemy1);
        enemy2 = BitmapFactory.decodeResource(res, R.drawable.enemy2);
        enemy3 = BitmapFactory.decodeResource(res, R.drawable.enemy3);
        enemy4 = BitmapFactory.decodeResource(res, R.drawable.enemy4);
        enemy5 = BitmapFactory.decodeResource(res, R.drawable.enemy5);
        enemy6 = BitmapFactory.decodeResource(res, R.drawable.enemy6);
        enemy7 = BitmapFactory.decodeResource(res, R.drawable.enemy7);

        // for collision properties
        width = enemy1.getWidth();
        height = enemy1.getHeight();


        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);


        enemy1 = Bitmap.createScaledBitmap(enemy1, width, height, false);
        enemy2 = Bitmap.createScaledBitmap(enemy2, width, height, false);
        enemy3 = Bitmap.createScaledBitmap(enemy3, width, height, false);
        enemy4 = Bitmap.createScaledBitmap(enemy4, width, height, false);
        enemy5 = Bitmap.createScaledBitmap(enemy5, width, height, false);
        enemy6 = Bitmap.createScaledBitmap(enemy6, width, height, false);
        enemy7 = Bitmap.createScaledBitmap(enemy7, width, height, false);

        y = -height;
    }

    Bitmap getEnemy () {


        if (enemyCounter == 1) {
            enemyCounter++;
            return enemy1;
        }

        if (enemyCounter == 2) {
            enemyCounter++;
            return enemy2;
        }

        if (enemyCounter == 3) {
            enemyCounter++;
            return enemy3;
        }

        if (enemyCounter == 4) {
            enemyCounter++;
            return enemy4;
        }

        if (enemyCounter == 5) {
            enemyCounter++;
            return enemy5;
        }

        if (enemyCounter == 6) {
            enemyCounter++;
            return enemy6;
        }

        enemyCounter = 1;
        return enemy7;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
