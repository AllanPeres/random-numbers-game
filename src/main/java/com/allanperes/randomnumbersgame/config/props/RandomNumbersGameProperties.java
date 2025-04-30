package com.allanperes.randomnumbersgame.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.allanperes.randomnumbersgame")
public class RandomNumbersGameProperties {

    private GameProperties gameProperties = new GameProperties();

    @Data
    public static class GameProperties {
        private long timingBeforeTicking = 1000L;
        private int tickingTimes = 10;
    }

}
