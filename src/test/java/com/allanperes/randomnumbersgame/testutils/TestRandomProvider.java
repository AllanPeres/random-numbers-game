package com.allanperes.randomnumbersgame.testutils;

import com.allanperes.randomnumbersgame.utils.RandomProvider;
import java.util.Random;

public class TestRandomProvider implements RandomProvider {

    private Integer forcedValue;
    private final Random random = new Random();


    @Override
    public int nextRandom(int min, int max) {
        if (forcedValue != null) {
            return forcedValue;
        }
        return random.nextInt(max - min + 1) + min;
    }

    public void setForcedValue(int forcedValue) {
        this.forcedValue = forcedValue;
    }

    public void resetForcedValue() {
        this.forcedValue = null;
    }
}
