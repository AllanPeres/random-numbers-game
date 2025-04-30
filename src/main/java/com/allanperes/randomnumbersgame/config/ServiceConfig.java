package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.service.GameService;
import com.allanperes.randomnumbersgame.service.RulesService;
import com.allanperes.randomnumbersgame.utils.GameData;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public GameService gameService(GameTimer gameTimer,
                                   RulesService rulesService,
                                   GameData gameData) {
        return new GameService(rulesService, gameTimer, gameData);
    }

    @Bean
    public RulesService rulesService(RandomProvider randomProvider) {
        return new RulesService(randomProvider);
    }

}
