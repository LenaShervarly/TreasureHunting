package com.home.jsquad.knowhunt.location;

public enum TrackerCommand {

    Start(1<<0),
    Stop( 1<<1);


    private final int value;
    TrackerCommand(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
