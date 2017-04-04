package com.home.knowhunt.gameworld;

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
    private com.home.knowhunt.gameobjects.Wall wall;
    private com.home.knowhunt.gameobjects.Wall wall2;
    private com.home.knowhunt.gameobjects.Wall wall3;
    private com.home.knowhunt.gameobjects.Wall2 wall4;
    private com.home.knowhunt.gameobjects.Wall2 wall5;



    public void generateMap (int blocksCount) {
        com.home.knowhunt.gameobjects.Wall wall = new com.home.knowhunt.gameobjects.Wall();
        com.home.knowhunt.gameobjects.Wall2 wall2;

        int y = 10;
        int x = 10;
        for (int i = 0; i < 5; i++) {
            int rand = (int) (Math.random() * 150);
            int rand2 = (int) (Math.random() * 150);
            for (int j = 0; j < 4; j++) {
                wall = new com.home.knowhunt.gameobjects.Wall(x+(x*j)+rand,y );
                gp.getObstacles().add(wall);
            }
            x+=rand;
            wall2=new com.home.knowhunt.gameobjects.Wall2(y+rand2,wall.getX());
            gp.getObstaclesv().add(wall2);
            y+=rand2;
            wall2=new com.home.knowhunt.gameobjects.Wall2(y,wall.getX()+40);
            gp.getObstaclesv().add(wall2);

        }



        }




    public GameWorld(int midPointY) {
        this.midPointY = midPointY;
        currentState = GameState.READY;

        boy = new com.home.knowhunt.gameobjects.Boy(33, midPointY - 5, 15, 15, midPointY * 2);

        gp = new GamePlay(boy);
        end= new com.home.knowhunt.gameobjects.Goal();
        end.setNode(100,100);
        generateMap(3);

        /*
        wall=new com.home.knowhunt.gameobjects.Wall();
        wall.setX(20);
        wall.setY(50);

        wall2= new com.home.knowhunt.gameobjects.Wall();
        wall2.setX(60);
        wall2.setY(80);

        wall3= new com.home.knowhunt.gameobjects.Wall();
        wall3.setX(50);
        wall3.setY(20);

        wall4= new com.home.knowhunt.gameobjects.Wall2();
        wall3.setX(60);
        wall3.setY(80);

        wall5= new com.home.knowhunt.gameobjects.Wall2();
        wall3.setX(50);
        wall3.setY(20);
/*

       gp.getObstacles().add(wall);
      gp.getObstacles().add(wall2);
       gp.getObstacles().add(wall3);
       gp.getObstacles().add(wall4);
       gp.getObstacles().add(wall5);

*/
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
