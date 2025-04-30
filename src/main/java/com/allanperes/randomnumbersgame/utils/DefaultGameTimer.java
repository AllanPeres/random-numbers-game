package com.allanperes.randomnumbersgame.utils;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGameTimer implements GameTimer {

    private final RandomNumbersGameProperties properties;

    private final AtomicLong msPassed = new AtomicLong(0);

    @Override
    public boolean isFinished() {
        return msPassed.get() >= timeLimitInMs();
    }

    @Override
    public int remainingTimeInSeconds() {
        return timeLimitInSeconds() - timePassedInSeconds();
    }

    @Override
    public void resetTimer() {
        msPassed.set(0);
    }

    @Override
    public void increaseTime(long waitingTimeInMs) {
        msPassed.addAndGet(waitingTimeInMs);
    }

    @Override
    public void increaseTime() {
        increaseTime(properties.getGameProperties().getTimingBeforeTicking());
    }

    private int timePassedInSeconds() {
        return Math.round((float) msPassed.get() / 1000);
    }

    private int timeLimitInSeconds() {
        var tickingTimeInSeconds = (int)(properties.getGameProperties().getTimingBeforeTicking() / 1000);
        return properties.getGameProperties().getTickingTimes() * tickingTimeInSeconds;
    }

    private long timeLimitInMs() {
        var tickingTimeInSeconds = (properties.getGameProperties().getTimingBeforeTicking());
        return properties.getGameProperties().getTickingTimes() * tickingTimeInSeconds;
    }
}
