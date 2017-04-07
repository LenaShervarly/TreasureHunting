package com.home.knowhunt.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.home.knowhunt.mazehelpers.AssetLoader;


public class Goal extends Autonomous
{
    private float lvl = 0.2f;
    private int score = 1;
    private float lvlInc = 0.2f;
    private float maxLvl = 1.6f;

    public static final int width = 15;
    public static final int height = 15;

    public Goal() {
        super(width, height);
        setFactor(1000);
        setDivider(97);
    }




    public void update(float delta) {
        move(delta + lvl);
    }


    public TextureRegion getTexture() { return AssetLoader.goal; }

    public void playSound() { AssetLoader.coin.play(); }


}
