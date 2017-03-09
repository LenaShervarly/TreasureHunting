package com.home.croaton.followme.audio;

public enum AudioServiceCommand
{
    Play(           1<<0),
    Pause(          1<<1),
    Stop(           1<<2),
    Rewind(         1<<3),
    LoadTracks(     1<<4),
    ToggleState(    1<<5),
    StartForeground(1<<6);

    private final int value;
    AudioServiceCommand(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
