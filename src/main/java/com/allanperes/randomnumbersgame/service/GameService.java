package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.models.GameData;
import com.allanperes.randomnumbersgame.models.GameState;
import com.allanperes.randomnumbersgame.models.User;
import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import com.allanperes.randomnumbersgame.models.dto.WinningsDto;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    private final RulesService rulesService;
    private final GameTimer gameTimer;

    private GameState gameState = new GameState();
    private GameData gameData = new GameData();

    public void addUser(User user, WebSocketSession session) {
        users.put(user.username(), user);
        webSocketSessions.put(user.username(), session);
    }

    public void removeUser(User user) {
        users.remove(user.username());
        webSocketSessions.remove(user.username());
    }

    public String getUserName() {
        return "player-" + UUID.randomUUID();
    }

    public synchronized void checkIfRoundHasStartedOrStart(WebSocketSession session) {
        if (gameState.isRoundRunning()) {
            sendMessage(session, new TextMessage("Round already started, you have " +
                    gameTimer.remainingTime() + " seconds to place a bet!"));
            return;
        }
        if (!gameState.isRoundRunning() && !webSocketSessions.isEmpty()) {
            startRound();
        }
    }

    private synchronized void startRound() {
        if (gameState.isRoundRunning()) {
            return;
        }
        log.info("Starting round");
        gameState.startRound();
        gameState = new GameState();
        gameTimer.resetTimer();
        for (WebSocketSession webSocketSession : webSocketSessions.values()) {
            sendMessage(webSocketSession, new TextMessage("Game started! You have 10 seconds to give your guess!"));
        }
        CompletableFuture.runAsync(this::runRound);
    }

    private void runRound() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                gameTimer.increaseSecond();
                // If game state change to not running, need to stop counting.
                if (!gameState.isRoundRunning() || gameTimer.isFinished()) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        finishRound();
    }

    public synchronized void finishRound() {
        if (!gameState.isRoundRunning()) {
            return;
        }
        log.info("Finishing round");
        gameState.stopRound();
        gameTimer.resetTimer();
        verifyGuesses();
        notifyUsers();
        startRound();
    }

    public void makeBet(WebSocketSession session, GuessDto guess) {
        if (gameState.isRoundRunning()) {
            final var username = session.getAttributes().get("username").toString();

            gameData.addGuess(username, (e) -> {
                log.info("User {} made a bet on {}", username, guess);
                sendMessage(session, new TextMessage("Your guess is " + guess.guess() + " you bet " + guess.bet()));
                return guess;
            });
        }
    }

    private void verifyGuesses() {
        int winnerNumber = rulesService.calculateWinnerNumber();
        gameData.calculateWinnersAndLosers(winnerNumber, rulesService::calculateWinnings);
    }

    private void notifyUsers() {
        for (WinningsDto winner : gameData.getWinners()) {
            final var session = webSocketSessions.get(winner.username());
            if (session != null) {
                sendMessage(session, new TextMessage("You won " + winner.winnings()));
            }
        }
        for (String username : gameData.getLosers()) {
            final var session = webSocketSessions.get(username);
            if (session != null) {
                sendMessage(session, new TextMessage("You lost, don't be sad try one more time!"));
            }
        }
        for (WebSocketSession session : webSocketSessions.values()) {
            final var winnerText = new TextMessage("The winners are " + gameData.getWinners());
            sendMessage(session, winnerText);
        }
    }

    private void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



}
