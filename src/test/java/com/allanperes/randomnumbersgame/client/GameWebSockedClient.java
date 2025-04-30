package com.allanperes.randomnumbersgame.client;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class GameWebSockedClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameWebSockedClient.class);
    WebSocketClient webSocketClient = new StandardWebSocketClient();
    WebSocketSession session;

    public WebSocketSession connect() {
        try {
            session = webSocketClient.doHandshake(new TextWebSocketHandler() {
                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) {
                    LOGGER.info("received message - " + message.getPayload());
                }

                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    LOGGER.info("established connection - " + session);
                }
            }, new WebSocketHttpHeaders(), URI.create("ws://localhost:8080/game")).get();

        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
        return session;
    }
}
