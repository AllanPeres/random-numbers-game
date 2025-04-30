package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import com.allanperes.randomnumbersgame.testutils.TestGameTimer;
import com.allanperes.randomnumbersgame.testutils.TestRandomProvider;
import com.allanperes.randomnumbersgame.utils.GameData;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@EnableConfigurationProperties(RandomNumbersGameProperties.class)
public class IntegrationConfiguration {

    @Bean
    @Primary
    GameTimer gameTimer(RandomNumbersGameProperties properties) {
        return new TestGameTimer(properties);
    }

    @Bean
    @Primary
    RandomProvider randomProvider() {
        return new TestRandomProvider();
    }
}
