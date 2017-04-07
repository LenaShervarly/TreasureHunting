package com.home.knowhunt.gameworld;

import com.home.knowhunt.gameobjects.Wall;
import com.home.knowhunt.gameobjects.Wall2;
import com.home.knowhunt.mazehelpers.AssetLoader;

import java.util.LinkedList;


public class GameWorld {

    private com.home.knowhunt.gameobjects.Boy boy;
    private com.home.knowhunt.gameobjects.Goal end;
    public enum GameState { READY, RUNNING, GAMEOVER, HIGHSCORE }
    private boolean win;
    private GameState currentState;
    private GamePlay gp;

    private int midPointY;

    public void generateMap () {
        Wall wall;
        Wall2 wall2;

        int y = 30;
        int x = 43;
        int j=0;
        for (int i = 0; i < 3; i++) {
            while(j<7){
                wall = new Wall(i*x,j*y );
                gp.getObstacles().add(wall);
                wall2=new Wall2(i*x,j*y);
                gp.getObstaclesv().add(wall2);
                j++;
            }


            j=0;
        }

    }

    public void removeWalls () {
        int offset=0;

        int[] rowsToRemove = {10,11,15,19,8,9,16,18,7,6,5,3,2,0};
        int[] columnToRemove = {4,5,6,11,12,7,4,2,20,1,10,18,16,15};

        for (int i = 0; i < rowsToRemove.length; i++) {
            //        offset= (int)(Math.random()*20);     // this would make the walls removal random
            getObstacles().get(Math.abs(rowsToRemove[i]-offset)).setY(500);
            getObstaclesv().get(Math.abs(columnToRemove[i]-offset)).setY(500);
        }
    }





    public GameWorld(int midPointY) {
        this.midPointY = midPointY;
        currentState = GameState.READY;

        boy = new com.home.knowhunt.gameobjects.Boy(33, midPointY - 5, 15, 15, midPointY * 2);

        gp = new GamePlay(boy);
        end= new com.home.knowhunt.gameobjects.Goal();
        end.setNode(110,5);
        generateMap();
        removeWalls();

    }


    public void update(float delta) {
        switch (currentState) {
            case READY:
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }

    }


    public void updateRunning(float delta) {
        boy.update(delta);
        for(com.home.knowhunt.gameobjects.Autonomous a : gp.getObstacles()) {
            if (boy.collides(a)) { a.playSound();gameOver();return;}
        }

        for(com.home.knowhunt.gameobjects.Autonomous a : gp.getObstaclesv()) {
            if (boy.collides(a)) { a.playSound();gameOver();return;}
        }

        if (boy.collides(end)) {
            win=true; end.playSound();gameOver(); return;
        }
    }

    private void gameOver() {
        boy.die();
        AssetLoader.dead.play();
        currentState = GameState.GAMEOVER;
    }

    public com.home.knowhunt.gameobjects.Goal getEnd () {return end;}

    public void restart() {
        currentState = GameState.READY;
        boy.onRestart(midPointY - 5);
        currentState = GameState.READY;
    }

    public void start() { currentState = GameState.RUNNING; }


    public com.home.knowhunt.gameobjects.Boy getBoy() { return boy; }
    public LinkedList<com.home.knowhunt.gameobjects.Autonomous> getObstacles() { return gp.getObstacles(); }
    public LinkedList<com.home.knowhunt.gameobjects.Autonomous> getObstaclesv() { return gp.getObstaclesv(); }


    public boolean isReady() { return currentState == GameState.READY; }
    public boolean isGameOver() { return currentState == GameState.GAMEOVER; }
    public boolean isWon() {return win;}
}
