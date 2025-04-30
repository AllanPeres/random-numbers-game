package com.allanperes.randomnumbersgame.testutils;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class TestGameTimer implements GameTimer {

    private final RandomNumbersGameProperties properties;
    private long timeBeforeTicking;
    private int tickingTimes;

    public TestGameTimer(RandomNumbersGameProperties properties) {
        this.properties = properties;
        timeBeforeTicking = properties.getGameProperties().getTimingBeforeTicking();
        tickingTimes = properties.getGameProperties().getTickingTimes();
    }

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
        increaseTime(timeBeforeTicking);
    }

    @Override
    public int getTicklingTimes() {
        return tickingTimes;
    }

    @Override
    public long getTimeBeforeTicking() {
        return timeBeforeTicking;
    }

    private int timePassedInSeconds() {
        return Math.round((float) msPassed.get() / 1000);
    }

    private int timeLimitInSeconds() {
        var tickingTimeInSeconds = (int)(timeBeforeTicking / 1000);
        return properties.getGameProperties().getTickingTimes() * tickingTimeInSeconds;
    }

    private long timeLimitInMs() {
        return properties.getGameProperties().getTickingTimes() * timeBeforeTicking;
    }

    public void setTimeBeforeTicking(long timeBeforeTicking) {
        this.timeBeforeTicking = timeBeforeTicking;
    }

    public void setTickingTimes(int tickingTimes) {
        this.tickingTimes = tickingTimes;
    }
}
