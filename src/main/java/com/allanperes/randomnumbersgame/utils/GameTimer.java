package com.allanperes.randomnumbersgame.utils;

public interface GameTimer {

    int timeElapsed();
    int remainingTime();
    boolean isFinished();
    void resetTimer();
    void increaseSecond();
}
