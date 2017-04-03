package com.home.knowhunt.gameobjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.home.knowhunt.shared.Directions;


public abstract class Obstacle {

    private Node head;
    private int width;
    private int height;
    private int gameHeight;
    private boolean isAlive;
    private float rotation;
    private float lvl = 0.4f;
    private float lvlInc = 0.2f;
    private float maxLvl = 1.6f;

    public Obstacle(float x, float y, int width, int height) {
        head = new Node(new Vector2(x, y), Directions.NORTH);
        this.width = width;
        this.height = height;

        isAlive = true;
        rotation = 0f;
    }

    public void move(float delta) {
        switch (head.getDir()) {
            case NORTH:
                head.setY(head.getY() - delta);
                break;
            case EAST:
                head.setX(head.getX() + delta);
                break;
            case SOUTH:
                head.setY(head.getY() + delta);
                break;
            case WEST:
                head.setX(head.getX() - delta);
                break;
        }
    }

    /* x, y getters and setters */
    public float getX() { return head.getX(); }
    public float getY() { return head.getY(); }
    public void setX(float x) { head.setX(x); }
    public void setY(float y) { head.setY(y); }

    /* Directions getter and setter */
    public Directions getDir() { return head.getDir(); }
    public void setDir(Directions d) { head.setDir(d); }

    /* Node getter and setter */
    public Node getNode() { return head; }
    public void setNode(float x, float y) {
        head = new Node(new Vector2(x, y), getDir());
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setGameHeight(int height) { this.gameHeight = height; }

    public void setAlive(boolean b) { isAlive = b; }
    public boolean isAlive()  { return isAlive; }

    public void setRotation(Directions d) { rotation = getRotation(d); }

    public static float getRotation(Directions d) {
        switch(d) {
            case NORTH: return -90;
            case SOUTH: return 90;
            case WEST: return 180;
            case EAST: return 0;
            default: return 0;
        }
    }

    public Rectangle getBoundingCircle() {return new Rectangle(getX(), getY(),
            getWidth() / 4.0f, getHeight() / 4.0f
            ); }

    public abstract void update(float delta);

    public boolean collides(Obstacle c) {
        return Intersector.overlaps(getBoundingCircle(), c.getBoundingCircle());
    }

    public boolean collides(Node n) {
        return Intersector.overlaps( n.getBoundingCircle(),getBoundingCircle());
    }
}
