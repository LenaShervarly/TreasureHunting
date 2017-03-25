package com.home.croaton.followme.database;

public class Player {

    private int ID;
    private String name;
    private String surname;
    private String eMail;
    private String username;
    private String password;
    private int scores;

    public Player(){
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
        return 0;
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




}
