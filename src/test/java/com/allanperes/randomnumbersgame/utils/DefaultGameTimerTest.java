package com.allanperes.randomnumbersgame.utils;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultGameTimerTest {

    private DefaultGameTimer timer;

    @Test
    void when_TimeElapsed_should_ReturnCorrectValues() {
        timer = new DefaultGameTimer();

        timer.increaseSecond();
        timer.increaseSecond();
        timer.increaseSecond();

        assertEquals(3, timer.timeElapsed());
        assertEquals(7, timer.remainingTime());
        assertFalse(timer.isFinished());
    }

    @Test
    void when_TimerIsReset_shouldReturnCorrectValues() {
        timer = new DefaultGameTimer();

        timer.increaseSecond();
        timer.increaseSecond();
        timer.increaseSecond();

        timer.resetTimer();

        assertEquals(0, timer.timeElapsed());
        assertEquals(10, timer.remainingTime());
        assertFalse(timer.isFinished());
    }

    @Test
    void when_TimeElapsedIs10_shouldReturnIsFinished() {
        timer = new DefaultGameTimer();

        IntStream.range(0, 10).forEach(i -> timer.increaseSecond());

        assertEquals(10, timer.timeElapsed());
        assertEquals(0, timer.remainingTime());
        assertTrue(timer.isFinished());
    }
}
