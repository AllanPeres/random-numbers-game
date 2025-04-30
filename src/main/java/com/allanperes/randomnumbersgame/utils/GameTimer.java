package com.allanperes.randomnumbersgame.utils;

public interface GameTimer {

    int remainingTimeInSeconds();
    boolean isFinished();
    void resetTimer();
    void increaseTime(long waitingTimeInMs);
    void increaseTime();
}
