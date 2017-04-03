package com.home.knowhunt.maze;

import com.badlogic.gdx.Game;

public class MazeGame extends Game {

    public static final double version = 0.02;

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
}
