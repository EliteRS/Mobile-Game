package com.example.game;

import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.media.AudioAttributes;
        import android.media.AudioManager;
        import android.media.SoundPool;
        import android.os.Build;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.view.SurfaceView;
        import android.view.View;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private Background background1;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Enemy[] enemies;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private int sound;
    private Player player;
    private Deflect deflect;
    private GameActivity activity;
    int d = 0;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.fireball, 1);

        this.screenX = screenX;
        this.screenY = screenY;

        //portrait
        screenRatioX = 1080f / screenX;  // screen ratio assuming the screen width is 1080 MAX
        screenRatioY = 1920f / screenY;  // screen ratio assuming the screen height is 1920 MAX

        background1 = new Background(screenX, screenY, getResources());

        //resources for player and deflect
        player = new Player(this, screenY, getResources());
        deflect = new Deflect(this, screenY, getResources());


        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        //enemy spawns
        enemies = new Enemy[4];

        for (int i = 0;i < 4;i++) {
            Enemy enemy = new Enemy(getResources());
            enemies[i] = enemy;
        }

        random = new Random();
        enemies[0].x = 700;
        enemies[0].y += 30;
        enemies[1].x = 200;
        enemies[1].y -= 300;
        enemies[2].x = 400;
        enemies[2].y -= 200;

    }

    @Override
    public void run() {
        while (isPlaying) {
            update (); //update the character locations
            draw (); // draw the characters
            sleep (); // delay to run 60fps

        }
    }

    private void update () {

        //player & deflect going left or right
        if (player.isGoingRight && deflect.isGoingRight) {
            player.x += 30 * screenRatioX;
            deflect.x += 30 * screenRatioX;
        }
        if (player.isGoingLeft && deflect.isGoingLeft) {
            player.x -= 30 * screenRatioX;
            deflect.x -= 30 * screenRatioX;
        }

        //if deflect is true
        if(deflect.isGoingDeflect){
            deflect.toShoot++;
            //each enemy kay maigo sa collision sa deflect instead sa player
            for (int i = 0; i < 4; i++) {
                if (Rect.intersects(enemies[i].getCollisionShape(), deflect.getCollisionShape())) {
                    isGameOver = false;
                    score++;
                    enemies[i].y = 2500;
                    return;
                }
            }
        }

        //wall
        if (player.x < 0 && deflect.x<0) {
            player.x = 0;
            deflect.x = 0;
        }
        if (player.x >= screenX - player.width && deflect.x >= screenX - deflect.width) {
            player.x = screenX - player.width;
            deflect.x = screenX - deflect.width;
        }

        //each enemy na mu landing or crash sa ubos kay ma game over
        for (Enemy enemy : enemies){
            if (enemy.y + enemy.height >= 2000 && enemy.y + enemy.height <= 2400){
                isGameOver = true;
            }
        }

        if (d == 0) {
            for (Enemy enemy : enemies) {
                //speed kay y kay pa ubos mn
                enemy.y += enemy.speed;

                //mu spawn ug another enemy with different speed and location
                if (enemy.y + enemy.height >= 2500) {
                    int bound = (int) (30 * screenRatioY);
                    enemy.speed = random.nextInt(bound);

                    if (enemy.speed < 10 * screenRatioX)
                        enemy.speed = (int) (15 * screenRatioX);

                    enemy.x = random.nextInt(screenX - enemy.width);
                    enemy.y = 0;
                }

            // if maigo and enemy sa player kay ma game over
            if (Rect.intersects(enemy.getCollisionShape(), player.getCollisionShape()))
                isGameOver = true;
            }
        }
        d=0;
    }

//para mu draw ang mga characters, background
    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);

            for (Enemy enemy : enemies)
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(player.getDead(), player.x, player.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            //mu appear ang deflect within loop nya ma exit
            while(deflect.toShoot>0){
                canvas.drawBitmap(deflect.getDeflect(), deflect.x, deflect.y, paint);
            }

            canvas.drawBitmap(player.getPlayer(), player.x, player.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    //para inig gameover kay mu exit cya
    private void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //para highscore
    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }


    private void sleep () {
        try {
            Thread.sleep(20);//17
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume () {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            //action down means para ma determine if ang finger kay naa pas screen
            case MotionEvent.ACTION_DOWN:
                // divide 2 na cya sa width para maka go right
                if (x > getWidth() / 2) {
                    player.isGoingRight = true;
                    deflect.isGoingRight = true;
                }

                // divide 2 na cya sa width para maka go left
                if (x < getWidth() /2){
                    player.isGoingLeft = true;
                    deflect.isGoingLeft = true;

                }

                // divide 2 na cya sa width para maka hold/tap up
                if(y< getHeight()/2){
                    player.isGoingRight = false;
                    player.isGoingLeft = false;
                    deflect.isGoingRight = false;
                    deflect.isGoingLeft = false;
                    deflect.isGoingDeflect = true;
                    d++;
                }

                break;

            //action up means para ma determine if ang finger kay wa na sa screen
            case MotionEvent.ACTION_UP:
                if (x > getWidth() / 2) {
                    player.isGoingRight = false;
                    deflect.isGoingRight = false;
                }

                if (x < getWidth() /2){
                    player.isGoingLeft = false;
                    deflect.isGoingLeft = false;

                }
                if(y< getHeight()/2){
                player.isGoingRight = false;
                player.isGoingLeft = false;
                deflect.isGoingRight = false;
                deflect.isGoingLeft = false;
                deflect.isGoingDeflect = false;
                d++;
            }
                break;
        }

        return true;
    }

    // para sa sound sa deflect
    public void newSound() {
        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);
      }
}
