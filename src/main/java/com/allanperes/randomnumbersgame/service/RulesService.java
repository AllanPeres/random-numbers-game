package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.utils.RandomProvider;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RulesService {

    private final RandomProvider randomProvider;
    private final BigDecimal STAKE_RATIO = BigDecimal.valueOf(9.9);
    private final Integer MIN_GUESS = 1;
    private final Integer MAX_GUESS = 10;
    private int winnerNumber;

    public void calculateWinnerNumber() {
        winnerNumber = randomProvider.nextRandom(MIN_GUESS, MAX_GUESS);
    }

    public boolean isWinner(int guess) {
        return winnerNumber == guess;
    }

    public BigDecimal calculateWinnings(BigDecimal bet) {
        if (bet.compareTo(BigDecimal.ONE) < 1) {
            throw new IllegalArgumentException("Bet must be greater than one");
        }
        return bet.multiply(STAKE_RATIO);
    }

}
