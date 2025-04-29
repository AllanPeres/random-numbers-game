package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.handlers.GameWebSocketHandler;
import com.allanperes.randomnumbersgame.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameService gameService;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler(), "/game");
    }

    @Bean
    public GameWebSocketHandler gameWebSocketHandler() {
        return new GameWebSocketHandler(gameService, objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
