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
        x = random.nextInt(width - 10);
        if (x < 10) x = 10;
        y = random.nextInt(height - 10);
        if (y < 10) y = 10;
        power = random.nextInt(8) + 1;

        if (power == 1) {
            r = 100;
            g = 0;
            b = 0;
        }
        if (power == 2) {
            r = 0;
            g = 170;
            b = 0;
        }
        if (power == 3) {
            r = 0;
            g = 255;
            b = 0;
        }
        if (power == 4) {
            r = 100;
            g = 0;
            b = 0;
        }
        if (power == 5) {
            r = 170;
            g = 0;
            b = 0;
        }
        if (power == 6) {
            r = 255;
            g = 0;
            b = 0;
        }
        if (power == 7) {
            r = 0;
            g = 0;
            b = 100;
        }
        if (power == 8) {
            r = 0;
            g = 0;
            b = 170;
        }
        if (power == 9) {
            r = 0;
            g = 0;
            b = 255;
        }
    }
}
