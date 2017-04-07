package com.home.knowhunt.maze;

import com.badlogic.gdx.Game;

public class MazeGame extends Game {

    public static final double version = 0.02;
    public static MazeGame.MyGameCallback myGameCallback;

    @Override
    public void create() {
        com.home.knowhunt.mazehelpers.AssetLoader.load();
        setScreen(new com.home.knowhunt.screens.GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        com.home.knowhunt.mazehelpers.AssetLoader.dispose();
    }

    // Define an interface for your various callbacks to the android launcher
    public interface MyGameCallback {

        public void onGameEnd(boolean win);
    }

    public static void setMyGameCallback(MazeGame.MyGameCallback callback) {
        myGameCallback = callback;
    }



}
