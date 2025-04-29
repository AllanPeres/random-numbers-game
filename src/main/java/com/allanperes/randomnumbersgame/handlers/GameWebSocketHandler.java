package com.allanperes.randomnumbersgame.handlers;

import com.allanperes.randomnumbersgame.models.GuessPayload;
import com.allanperes.randomnumbersgame.models.User;
import com.allanperes.randomnumbersgame.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameService gameService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        final var guess = objectMapper.readValue(message.getPayload(), GuessPayload.class);
        gameService.makeBet(session, guess);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        final var sessionId = session.getId();
        final var userName = gameService.getUserName();
        session.getAttributes().put("username", userName);
        gameService.addUser(new User(sessionId, userName), session);
        gameService.checkIfRoundHasStartedOrStart(session);
        session.sendMessage(new TextMessage("You have joined the game! You are " + userName + "!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        final var sessionId = session.getId();
        final var username = session.getAttributes().get("username").toString();
        gameService.removeUser(new User(sessionId, username));
    }
}
