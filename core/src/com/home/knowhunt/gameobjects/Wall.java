package com.home.knowhunt.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.home.knowhunt.mazehelpers.AssetLoader;

public class Wall extends Autonomous
{
    private float lvl = 0.3f;
    private int score = 2;
    private float lvlInc = 0.2f;
    private float maxLvl = 1.6f;

    public static final int width = 200;
    public static final int height = 15;

    public Wall() {
        super(width, height);
        setFactor(2000);
        setDivider(543);
    }

    public Wall (int x,int y){
        super(width, height);
        setX(x);
        setY(y);
    }



    public void update(float delta) {
        changeDir();
        move(delta + lvl);
    }


    public TextureRegion getTexture() { return AssetLoader.wall; }

    public void playSound() { AssetLoader.coin.play(); }

}
