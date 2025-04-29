package com.allanperes.randomnumbersgame.utils;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameTimer implements GameTimer {

    private final AtomicInteger secondsPassed = new AtomicInteger(0);
    private final Integer ROUND_TIME = 10;

    @Override
    public int timeElapsed() {
        return secondsPassed.get();
    }

    @Override
    public boolean isFinished() {
        return secondsPassed.get() >= 10;
    }

    @Override
    public int remainingTime() {
        return ROUND_TIME - secondsPassed.get();
    }

    @Override
    public void resetTimer() {
        secondsPassed.set(0);
    }

    @Override
    public void increaseSecond() {
        secondsPassed.incrementAndGet();
    }
}
