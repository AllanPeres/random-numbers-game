package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.testutils.TestRandomProvider;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RulesServiceTest {

    private RandomProvider randomProvider;
    private RulesService rulesService;

    @BeforeEach
    void setup() {
        randomProvider = new TestRandomProvider();
        rulesService = new RulesService(randomProvider);
    }

    @Test
    void when_calculateWinningsWithValidBet_should_ReturnWinningsCorrectly() {
        final var expectedWinnings = BigDecimal.valueOf(118.8);
        final var bet = BigDecimal.valueOf(12);

        assertEquals(0, expectedWinnings.compareTo(rulesService.calculateWinnings(bet)));
    }

    @Test
    void when_calculateWinningsWithInvalidBet_should_ThrowException() {
        final var bet = BigDecimal.valueOf(0.9999);

        assertThrows(IllegalArgumentException.class, () -> rulesService.calculateWinnings(bet));
    }

    @Test
    void when_IsWinnerIsCalledWithTheWinnerNumber_should_ReturnTrue() {
        ((TestRandomProvider) randomProvider).setForcedValue(3);
        rulesService.calculateWinnerNumber();

        assertTrue(rulesService.isWinner(3));
    }


}
