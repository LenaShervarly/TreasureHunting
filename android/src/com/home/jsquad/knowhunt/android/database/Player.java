package com.home.jsquad.knowhunt.android.database;

import android.support.annotation.NonNull;

public class Player implements Comparable<Player>{

    private int ID;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String eMail;
    private int scores;

    public Player(){
    }

    public Player(String name, String surname, String username, String password, String eMail) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.eMail = eMail;
        scores = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID){this.ID=ID;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Override
    public int compareTo(@NonNull Player another) {
        return getScores() > another.getScores() ? 1 : -1;
    }
}
