package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.handlers.GameWebSocketHandler;
import com.allanperes.randomnumbersgame.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler(), "/game");
    }

    @Bean
    public GameWebSocketHandler gameWebSocketHandler() {
        return new GameWebSocketHandler(gameService(), objectMapper());
    }

    @Bean
    public GameService gameService() {
        return new GameService();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
