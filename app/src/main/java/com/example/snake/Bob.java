package com.example.snake;

import java.util.Random;

public class Bob {
    public int x;
    public int y;
    public int r;
    public int g;
    public int b;
    public int power;

    public Bob(int width, int height){
        Random random = new Random();
        x = random.nextInt(width -1) + 1;
        if (x < 1) x = 1;
        y = random.nextInt(height - 1) + 1;
        if (y < 1) y = 1;
        power = random.nextInt(3) + 1;

        if (power == 1) {
            r = 0;
            g = 255;
            b = 0;
        }
        if (power == 2) {
            r = 255;
            g = 0;
            b = 0;
        }
        if (power == 3) {
            r = 0;
            g = 0;
            b = 255;
        }
    }

}
