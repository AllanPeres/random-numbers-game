package com.allanperes.randomnumbersgame.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    private GameState gameState;

    @Test
    void when_GameStateIsCreated_should_ReturnNotRunning() {
        gameState = new GameState();

        assertFalse(gameState.isRoundRunning());
    }

    @Test
    void when_GameStateIsStopped_should_ReturnNotRunning() {
        gameState = new GameState();

        assertFalse(gameState.isRoundRunning());

        gameState.stopRound();

        assertFalse(gameState.isRoundRunning());
    }

    @Test
    void when_GameStateIsStarted_should_ReturnRunning() {
        gameState = new GameState();

        assertFalse(gameState.isRoundRunning());

        gameState.startRound();

        assertTrue(gameState.isRoundRunning());
    }
}
