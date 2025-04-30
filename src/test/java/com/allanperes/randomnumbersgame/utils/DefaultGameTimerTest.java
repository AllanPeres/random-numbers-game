package com.allanperes.randomnumbersgame.utils;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultGameTimerTest {

    private DefaultGameTimer timer;

    @Test
    void when_TimeElapsed_should_ReturnCorrectValues() {
        timer = new DefaultGameTimer(new RandomNumbersGameProperties());

        timer.increaseTime();
        timer.increaseTime();
        timer.increaseTime();

        assertEquals(7, timer.remainingTimeInSeconds());
        assertFalse(timer.isFinished());
    }

    @Test
    void when_TimerIsReset_shouldReturnCorrectValues() {
        timer = new DefaultGameTimer(new RandomNumbersGameProperties());

        timer.increaseTime();
        timer.increaseTime();
        timer.increaseTime();

        timer.resetTimer();

        assertEquals(10, timer.remainingTimeInSeconds());
        assertFalse(timer.isFinished());
    }

    @Test
    void when_TimeElapsedIs10_shouldReturnIsFinished() {
        timer = new DefaultGameTimer(new RandomNumbersGameProperties());

        IntStream.range(0, 10).forEach(i -> timer.increaseTime());

        assertEquals(0, timer.remainingTimeInSeconds());
        assertTrue(timer.isFinished());
    }
}
