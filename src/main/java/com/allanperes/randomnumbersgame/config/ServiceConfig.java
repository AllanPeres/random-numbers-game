package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import com.allanperes.randomnumbersgame.service.GameService;
import com.allanperes.randomnumbersgame.service.RulesService;
import com.allanperes.randomnumbersgame.utils.DefaultGameTimer;
import com.allanperes.randomnumbersgame.utils.DefaultRandomProvider;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public GameService gameService(RandomNumbersGameProperties properties) {
        return new GameService(rulesService(), gameTimer(properties), properties);
    }

    @Bean
    public GameTimer gameTimer(RandomNumbersGameProperties properties) {
        return new DefaultGameTimer(properties);
    }

    @Bean
    public RulesService rulesService() {
        return new RulesService(randomProvider());
    }

    @Bean
    public RandomProvider randomProvider() {
        return new DefaultRandomProvider();
    }

}
