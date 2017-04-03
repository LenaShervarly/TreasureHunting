package com.home.knowhunt.mazehelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    public static Texture texture;
    public static TextureRegion bg;

    public static TextureRegion boyhead, boyshadow;
    public static TextureRegion goal, wall,wall2;

    public static Sound dead, coin, flap, goalfx, wallfx;
    public static BitmapFont font, shadow, fontSmall, shadowSmall;

    public static Preferences prefs;


    public static void load() {
        texture = new Texture(Gdx.files.internal("data/boytexture.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bg = new TextureRegion(new Texture((Gdx.files.internal("data/floor.png"))));
        bg.flip(false, true);

        boyhead = new TextureRegion(new Texture((Gdx.files.internal("data/boy.png"))));
        boyhead.flip(false, true);
        boyshadow = new TextureRegion(new Texture((Gdx.files.internal("data/boyshadow.png"))));
        boyshadow.flip(false, true);

        goal = new TextureRegion(new Texture((Gdx.files.internal("data/kth.png"))));
        goal.flip(false, true);
        wall = new TextureRegion(new Texture((Gdx.files.internal("data/wall2.png"))));
        wall.flip(false, true);
        wall2 = new TextureRegion(new Texture((Gdx.files.internal("data/wall.png"))));
        wall2.flip(false, true);

        dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
        wallfx = Gdx.audio.newSound(Gdx.files.internal("data/vulture.wav"));
        goalfx = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

        fontSmall = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        fontSmall.setScale(.10f, -.10f);

        prefs = Gdx.app.getPreferences("Maze");

    }

    public static void dispose() {
        texture.dispose();
        dead.dispose();
        coin.dispose();
        flap.dispose();
        font.dispose();
        shadow.dispose();
    }



}
