package com.home.croaton.followme.location;

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
