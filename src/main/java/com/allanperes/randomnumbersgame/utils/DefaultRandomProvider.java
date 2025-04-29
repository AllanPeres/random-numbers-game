package com.allanperes.randomnumbersgame.utils;

import java.util.Random;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DefaultRandomProvider implements RandomProvider {

    private final Random random = new Random();

    @Override
    public int nextRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
