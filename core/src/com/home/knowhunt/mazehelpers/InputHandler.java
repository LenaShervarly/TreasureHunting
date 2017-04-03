package com.home.knowhunt.mazehelpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.home.knowhunt.gameworld.GameRenderer;
import com.home.knowhunt.shared.Directions;
import com.home.knowhunt.gameobjects.Boy;


public class InputHandler implements InputProcessor {

    private com.home.knowhunt.gameworld.GameWorld world;
    private GameRenderer renderer;
    private Boy boy;
    Vector3 touchPoint;

    public InputHandler(com.home.knowhunt.gameworld.GameWorld world, GameRenderer renderer) {
        this.world = world;
        this.boy = world.getBoy();
        this.renderer = renderer;
        this.touchPoint = new Vector3();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        renderer.getCam().unproject(touchPoint.set(screenX, screenY, 0));

        if (world.isReady()) { world.start(); }

        boy.onClick(touchPoint.x, touchPoint.y);

        if (world.isGameOver() || world.isHighScore()) { world.restart(); }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case 19:
                world.getBoy().changeDir(Directions.NORTH);
                break;
            case 20:
                world.getBoy().changeDir(Directions.SOUTH);
                break;
            case 21:
                world.getBoy().changeDir(Directions.WEST);
                break;
            case 22:
                world.getBoy().changeDir(Directions.EAST);
                break;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override
    public boolean scrolled(int amount) { return false; }
}
