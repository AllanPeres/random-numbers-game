package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import com.allanperes.randomnumbersgame.testutils.TestGameTimer;
import com.allanperes.randomnumbersgame.testutils.TestRandomProvider;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import org.junit.jupiter.api.BeforeEach;

public class GameServiceTest {

    private GameService gameService;
    private RulesService rulesService;
    private RandomProvider randomProvider;
    private GameTimer gameTimer;
    private RandomNumbersGameProperties properties;

    @BeforeEach
    void setup() {
        properties = new RandomNumbersGameProperties();
        gameTimer = new TestGameTimer(properties);
        randomProvider = new TestRandomProvider();
        rulesService = new RulesService(randomProvider);

    }
}
