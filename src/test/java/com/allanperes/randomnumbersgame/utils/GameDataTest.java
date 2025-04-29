package com.allanperes.randomnumbersgame.utils;

import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameDataTest {

    private GameData gameData;

    @Test
    void when_GameDataIsCreated_should_ReturnEmpty() {
        gameData = new GameData();

        assertTrue(gameData.getGuesses().isEmpty());
        assertTrue(gameData.getWinners().isEmpty());
        assertTrue(gameData.getLosers().isEmpty());
    }

    @Test
    void when_AddedGuesses_shouldComputeCorrectly() {
        gameData = new GameData();
        var dummySession = new TestWebSocket();
        var guest = new GuessDto(1, BigDecimal.ONE);

        gameData.addGuess("user", (e) -> {
            dummySession.sendMessage(new TextMessage(e));
            return guest;
        });

        assertEquals(1, gameData.getGuesses().size());
        assertEquals(1, gameData.getGuesses().get("user").guess());
        assertEquals(BigDecimal.ONE, gameData.getGuesses().get("user").bet());
        assertEquals("user", dummySession.message);
    }

    @Test
    void when_CalculateWinnersAndLosers_shouldComputeCorrectly() {
        gameData = new GameData();
        var guest1 = new GuessDto(1, BigDecimal.ONE);
        var guest2 = new GuessDto(3, BigDecimal.ONE);

        gameData.addGuess("username1", (e) -> guest1);
        gameData.addGuess("username2", (e) -> guest2);

        gameData.calculateWinnersAndLosers((i) -> i == 1, b -> b.multiply(BigDecimal.ONE));

        assertEquals(1, gameData.getWinners().size());
        assertEquals("username1", gameData.getWinners().getFirst().username());
        assertEquals(1, gameData.getLosers().size());
        assertEquals("username2", gameData.getLosers().getFirst());
        assertEquals(2, gameData.getGuesses().size());

    }

    private class TestWebSocket {

        private String message;

        public void sendMessage(TextMessage message) {
            this.message = message.getPayload();
        }
    }
}
