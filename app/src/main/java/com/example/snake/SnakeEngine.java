package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.Random;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SnakeEngine extends SurfaceView implements Runnable {


    // Our game thread for the main game loop
    private Thread thread = null;

    // To hold a reference to the Activity
    private Context context;

    // for plaing sound effects
    private SoundPool soundPool;
    private int eat_bob = -1;
    private int snake_crash = -1;

    // For tracking movement Heading
    public enum Heading {UP, RIGHT, DOWN, LEFT}
    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // To hold the screen size in pixels
    private int screenX;
    private int screenY;

    // How long is the snake
    private int snakeLength;

    // Where is Bob hiding?
     private Bob[] bobs = new Bob[3];

    // The size in pixels of a snake segment
    private int blockSize;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 30;
    private int numBlocksHigh;

    // Control pausing between updates
    private long nextFrameTime;
    // Update the game 10 times per second
    private final long FPS = 12;
    // There are 1000 milliseconds in a second
    private final long MILLIS_PER_SECOND = 1000;
// We will draw the frame much more often

    // How many points does the player have
    private int score;
    private int highscore = 0;

    // The location in the grid of all the segments
    private int[] snakeXs;
    private int[] snakeYs;

    // Everything we need for drawing
// Is the game currently playing?
    private volatile boolean isPlaying;

    // A canvas for our paint
    private Canvas canvas;

    // Required to use canvas
    private SurfaceHolder surfaceHolder;

    // Some paint for our canvas
    private Paint paint;

    Bitmap oroBob ;
    Bitmap argentoBob ;
    Bitmap bronzoBob ;
    Bitmap snakepic ;


    public SnakeEngine(Context context, Point size) {
        super(context);

        context = context;

        screenX = size.x;
        screenY = size.y;

        // Work out how many pixels each block is
        blockSize = screenX / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        numBlocksHigh = screenY / blockSize;

        // Set the sound up
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            // Create objects of the 2 required classes
            // Use m_Context because this is a reference to the Activity
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the two sounds in memory
            descriptor = assetManager.openFd("get_mouse_sound.ogg");
            eat_bob = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("death_sound.ogg");
            snake_crash = soundPool.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize=1;


        // Initialize the drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        // If you score 200 you are rewarded with a crash achievement!
        snakeXs = new int[200];
        snakeYs = new int[200];

        // Start the game
        newGame();
    }
    public void newGame() {
        // Start with a single snake segment
        snakeLength = 1;
        snakeXs[0] = NUM_BLOCKS_WIDE / 2;
        snakeYs[0] = numBlocksHigh / 2;

        // Get Bob ready for dinner
        spawnBobs(-1);

        // Reset the score
        score = 0;

        // Setup nextFrameTime so an update is triggered
        nextFrameTime = System.currentTimeMillis();
    }
    public void spawnBobs(int bobIndex) {
        if (bobIndex == -1) {
            bobs[0] = new Bob(NUM_BLOCKS_WIDE, numBlocksHigh);
            bobs[1] = new Bob(NUM_BLOCKS_WIDE, numBlocksHigh);
            bobs[2] = new Bob(NUM_BLOCKS_WIDE, numBlocksHigh);
        }
        else
        {
            bobs[bobIndex] = new Bob(NUM_BLOCKS_WIDE, numBlocksHigh);
        }
    }

    private void eatBob(Bob b, int bobIndex){
        //  Got him!
        // Increase the size of the snake
        snakeLength += b.power;
        score = score + b.power;
        if (score > highscore) {highscore = score;}

        //replace Bob
        // This reminds me of Edge of Tomorrow. Oneday Bob will be ready!
        spawnBobs(bobIndex);
        //add to the score

        soundPool.play(eat_bob, 1, 1, 0, 0, 1);
    }
    public boolean updateRequired() {

        // Are we due to update the frame
        if(nextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            nextFrameTime =System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            // Return true so that the update and draw
            // functions are executed
            return true;
        }

        return false;
    }
    private void moveSnake(){
        // Move the body
        for (int i = snakeLength; i > 0; i--) {
            // Start at the back and move it
            // to the position of the segment in front of it
            snakeXs[i] = snakeXs[i - 1];
            snakeYs[i] = snakeYs[i - 1];

            // Exclude the head because
            // the head has nothing in front of it
        }

        // Move the head in the appropriate heading
        switch (heading) {
            case UP:
                snakeYs[0]--;
                break;

            case RIGHT:
                snakeXs[0]++;
                break;

            case DOWN:
                snakeYs[0]++;
                break;

            case LEFT:
                snakeXs[0]--;
                break;
        }
    }
    private boolean detectDeath(){
        // Has the snake died?
        boolean dead = false;

        // Hit the screen edge
        if (snakeXs[0] == -1) dead = true;
        if (snakeXs[0] >= NUM_BLOCKS_WIDE) dead = true;
        if (snakeYs[0] == -1) dead = true;
        if (snakeYs[0] == numBlocksHigh) dead = true;

        // Eaten itself?
        for (int i = snakeLength - 1; i > 0; i--) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (motionEvent.getX() >= screenX / 2) {
                    switch(heading){
                        case UP:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.UP;
                            break;
                    }
                } else {
                    switch(heading){
                        case UP:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.UP;
                            break;
                    }
                }
        }
        return true;
    }

    public void update() {
        // Did the head of the snake eat Bob?
        int idx = -1;
        for (Bob bob : bobs) {
            idx++;
            if (snakeXs[0] == bob.x && snakeYs[0] == bob.y) {
                eatBob(bob, idx);
                break;
            }
        }


        moveSnake();

        if (detectDeath()) {
            //start again
            soundPool.play(snake_crash, 1, 1, 0, 0, 1);

            ((SnakeActivity)context).finish();
        }
    }
    public void draw() {
        // Get a lock on the canvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen with Game Code School blue
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the color of the paint to draw the snake white
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Scale the HUD text
            paint.setTextSize(45);
            canvas.drawText("Score:" + score, 10, 70, paint);
            canvas.drawText("High Score:" + highscore, 400, 70, paint);

            // Draw the snake one block at a time

            // Set the color of the paint to draw Bob red


            // Draw Bob
            for (Bob bob : bobs) {
                paint.setColor(Color.argb(255, 10, 10, 10));
                canvas.drawCircle(
                        (bob.x * blockSize) + (blockSize/2),
                        (bob.y * blockSize) + (blockSize/2),
                        blockSize /2,
                        paint);
                paint.setColor(Color.argb(255, bob.r, bob.g, bob.b));
                canvas.drawCircle(
                        (bob.x * blockSize) + (blockSize/2),
                        (bob.y * blockSize) + (blockSize/2),
                        blockSize /4,
                        paint);

            }
            for (int i = 0; i < snakeLength; i++) {
                paint.setColor(Color.argb(255, 100,0,0));
                canvas.drawCircle(
                        (snakeXs[i] * blockSize) + (blockSize/2),
                        snakeYs[i] * blockSize + (blockSize/2),
                        blockSize /2,
                        paint);
                paint.setColor(Color.argb(255, 255,0,0));

                canvas.drawCircle(
                        (snakeXs[i] * blockSize) + (blockSize/2),
                        (snakeYs[i] * blockSize) + (blockSize/2),
                        blockSize /4,
                        paint);

            }


            // Unlock the canvas and reveal the graphics for this frame
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void run() {

        while (isPlaying) {

            // Update 10 times a second
            if(updateRequired()) {
                update();
                draw();
            }

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
}
