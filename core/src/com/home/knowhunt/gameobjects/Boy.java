package com.home.knowhunt.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.home.knowhunt.shared.Directions;

import java.util.LinkedList;

public class Boy extends Obstacle {

    private final int startBodySize = 4;
    private final float startLvl = 0.8f;

    private LinkedList<Node> body;
    private int bodySize = startBodySize;
    private float lvl = startLvl;
    private float lvlInc = 0.5f;

    public Boy(float x, float y, int width, int height, int gameHeight) {
        super(x, y, width, height);
        setGameHeight(gameHeight);
        setAlive(true);

        this.body = new LinkedList<Node>();
    }

    private void pushToBody( int max) {
        if (body.size() >= max) { body.removeFirst(); }
        body.add(new Node(new Vector2(getX(), getY()), getDir()));
    }

    public void changeDir(Directions d) {
        switch (d) {
            case NORTH:
                if (getDir() != Directions.SOUTH) {
                    setDir(d);
                    setRotation(Directions.NORTH);
                }
                break;
            case SOUTH:
                if (getDir() != Directions.NORTH) {
                    setDir(d);
                    setRotation(Directions.SOUTH);
                }
                break;
            case WEST:
                if (getDir() != Directions.EAST) {
                    setDir(d);
                    setRotation(Directions.WEST);
                }
                break;
            case EAST:
                if (getDir() != Directions.WEST) {
                    setDir(d);
                    setRotation(Directions.EAST);
                }
                break;
        }
    }

    public void update(float delta) {
        pushToBody(bodySize);
        move(delta + lvl);
    }

    public void onClick(float x, float y) {
        if (isAlive()) {
            switch (getDir()) {
                case EAST:
                case WEST:
                    if (y > getY()) {
                        changeDir(Directions.SOUTH);
                    } else {
                        changeDir(Directions.NORTH);
                    }
                    break;
                case NORTH:
                case SOUTH:
                    if (x > getX()) {
                        changeDir(Directions.EAST);
                    } else {
                        changeDir(Directions.WEST);
                    }
                    break;
            }
        }
    }

    public void onRestart(int y) {
        setRotation(Directions.WEST);
        setY(y);
        setAlive(true);
        body = new LinkedList<Node>();
        bodySize = startBodySize;
        lvl = startLvl;
    }

    public LinkedList<Node> getBody() { return body; }

    public void die() { setAlive(false); }
}
