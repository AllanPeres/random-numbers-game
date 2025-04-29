package com.allanperes.randomnumbersgame.service;

import java.math.BigDecimal;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class RulesService {

    private final Random random = new Random();
    private final BigDecimal STAKE_RATIO = BigDecimal.valueOf(9.9);
    private final Integer MIN_GUESS = 1;
    private final Integer MAX_GUESS = 10;

    public int calculateWinnerNumber() {
        return random.nextInt(MAX_GUESS - MIN_GUESS + 1) + MIN_GUESS;
    }

    public BigDecimal calculateWinnings(BigDecimal bet) {
        return bet.multiply(STAKE_RATIO);
    }

}
