package com.home.knowhunt.gameworld;

import com.home.knowhunt.gameobjects.Boy;

import java.util.LinkedList;

/**
 * Created by Aiham on 03/13/17.
 */
public class GamePlay {

    private LinkedList<com.home.knowhunt.gameobjects.Autonomous> obstacles;
    private LinkedList<com.home.knowhunt.gameobjects.Autonomous> obstaclesv;

    private Boy boy;
    private int inc = 0, maxObstaclesCount = 0;

    public GamePlay(Boy boy) {
        obstacles = new LinkedList<com.home.knowhunt.gameobjects.Autonomous>();
        obstaclesv=new LinkedList<com.home.knowhunt.gameobjects.Autonomous>();
        this.boy = boy;
    }




    public LinkedList<com.home.knowhunt.gameobjects.Autonomous> getObstacles() { return obstacles; }
    public LinkedList<com.home.knowhunt.gameobjects.Autonomous> getObstaclesv() { return obstaclesv; }

}
