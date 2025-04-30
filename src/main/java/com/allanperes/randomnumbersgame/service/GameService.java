package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.utils.GameData;
import com.allanperes.randomnumbersgame.utils.GameState;
import com.allanperes.randomnumbersgame.models.User;
import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import com.allanperes.randomnumbersgame.models.dto.WinningsDto;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameService {

    private final ConcurrentHashMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    private final RulesService rulesService;
    private final GameTimer gameTimer;
    private final GameData gameData;

    private final GameState gameState = new GameState();

    public void addUser(User user, WebSocketSession session) {
        gameData.addPlayer(user);
        webSocketSessions.put(user.username(), session);
    }

    public void removeUser(User user) {
        gameData.removePlayer(user);
        webSocketSessions.remove(user.username());
        if (webSocketSessions.isEmpty()) {
            gameState.stopRound();
            gameData.reset();
        }
    }

    public String getUserName() {
        return "player-" + UUID.randomUUID();
    }

    public synchronized void checkIfRoundHasStartedOrStart(WebSocketSession session) {
        if (gameState.isRoundRunning()) {
            sendMessage(session, new TextMessage("Round already started, you have " +
                    gameTimer.remainingTimeInSeconds() + " seconds to place a bet!"));
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
        gameData.reset();
        gameTimer.resetTimer();
        for (WebSocketSession webSocketSession : webSocketSessions.values()) {
            sendMessage(webSocketSession, new TextMessage("Game started! You have 10 seconds to give your guess!"));
        }
        CompletableFuture.runAsync(this::runRound);
    }

    private void runRound() {
        final var waitingTime = gameTimer.getTimeBeforeTicking();
        while(gameState.isRoundRunning() && !gameTimer.isFinished()) {
            try {
                Thread.sleep(waitingTime);
                gameTimer.increaseTime(waitingTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e); // Throwing cause something really went wrong
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
        rulesService.calculateWinnerNumber();
        gameData.calculateWinnersAndLosers(rulesService::isWinner, rulesService::calculateWinnings);
    }

    private void notifyUsers() {
        notifyAllWinnersTheirWinnings();
        notifyAllLosers();
        notifyAllUsersTheWinners();
    }

    private void notifyAllWinnersTheirWinnings() {
        for (WinningsDto winner : gameData.getWinners()) {
            final var session = webSocketSessions.get(winner.username());
            if (session != null) {
                sendMessage(session, new TextMessage("You won " + winner.winnings()));
            }
        }
    }

    private void notifyAllLosers() {
        for (String username : gameData.getLosers()) {
            final var session = webSocketSessions.get(username);
            if (session != null) {
                sendMessage(session, new TextMessage("You lost, don't be sad try one more time!"));
            }
        }
    }

    private void notifyAllUsersTheWinners() {
        final var winnersMessage = getWinnersMessage();
        for (WebSocketSession session : webSocketSessions.values()) {
            final var winnerText = new TextMessage(winnersMessage);
            sendMessage(session, winnerText);
        }
    }

    private String getWinnersMessage() {
        if (gameData.getWinners().isEmpty()) {
            return "No winners this round! Better luck next time.";
        }
        final var winnersList = gameData.getWinners()
                .stream()
                .map(WinningsDto::getWinningsLog)
                .collect(Collectors.joining(", "));
        return "The winners are " + winnersList;
    }

    private void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
