package com.home.jsquad.knowhunt.domain;

public class AudioTrack {
    private String name;

    public AudioTrack(){
        this("");
    }

    public AudioTrack(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
