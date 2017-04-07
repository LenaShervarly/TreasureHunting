package com.home.knowhunt.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.home.knowhunt.screens.GameScreen;

import java.util.Random;

/**
 * This class is used to represent shared properties between objects
 * on the game board
 * @author      Aiham Alkaseer
 * @version     1.0
 */
public abstract class Autonomous extends Obstacle {

    private static Random r = new Random();
    private int factor;
    private int divider;

    public Autonomous(int height, int width) {
        this(1000, 97, height, width);
    }
    public Autonomous(int factor, int divider, int height, int width) {
        super(randX(), randY(), width, height);
        this.factor = factor;
        this.divider = divider;
        r = new Random();
    }

    private static float randX() { return r.nextInt(GameScreen.GAME_WIDTH); }
    private static float randY() { return r.nextInt(GameScreen.getHeight()); }

    public void setFactor(int factor) { this.factor = factor; }
    public void setDivider(int divider) { this.divider = divider; }


    public abstract TextureRegion getTexture();


    public abstract void playSound();
}
