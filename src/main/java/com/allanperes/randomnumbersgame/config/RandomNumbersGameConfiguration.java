package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.config.props.RandomNumbersGameProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RandomNumbersGameProperties.class)
public class RandomNumbersGameConfiguration {
}
