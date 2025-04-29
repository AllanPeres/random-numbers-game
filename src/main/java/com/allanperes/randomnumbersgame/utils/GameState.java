package com.allanperes.randomnumbersgame.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameState {

    private final AtomicBoolean isRoundRunning;

    public GameState() {
        this.isRoundRunning = new AtomicBoolean(false);
    }

    public boolean isRoundRunning() {
        return isRoundRunning.get();
    }

    public void startRound() {
        isRoundRunning.set(true);
    }

    public void stopRound() {
        isRoundRunning.set(false);
    }
}
