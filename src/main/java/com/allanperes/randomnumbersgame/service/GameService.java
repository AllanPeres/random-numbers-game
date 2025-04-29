package com.allanperes.randomnumbersgame.service;

import com.allanperes.randomnumbersgame.models.GuessPayload;
import com.allanperes.randomnumbersgame.models.User;
import com.allanperes.randomnumbersgame.models.WinnerPayload;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameService {

    private final BigDecimal STAKE_RATIO = BigDecimal.valueOf(9.9);
    private final Integer ROUND_TIME = 10;
    private final Integer MIN_GUESS = 1;
    private final Integer MAX_GUESS = 10;

    // Using a hashmap here, so the user can increase
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    private final AtomicBoolean isRoundRunning = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, GuessPayload> guesses = new ConcurrentHashMap<>();

    private final List<WinnerPayload> winners = new CopyOnWriteArrayList<>();
    private final List<String> losers = new CopyOnWriteArrayList<>();

    private final AtomicInteger secondsPassed = new AtomicInteger(0);
    private final Random random = new Random();

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
        if (isRoundRunning.get()) {
            sendMessage(session, new TextMessage("Round already started, you have " +
                    (ROUND_TIME - secondsPassed.get()) + " seconds to place a bet!"));
            return;
        }
        if (!isRoundRunning.get() && !webSocketSessions.isEmpty()) {
            startRound();
        }
    }

    private synchronized void startRound() {
        if (isRoundRunning.get()) {
            return;
        }
        log.info("Starting round");
        isRoundRunning.set(true);
        guesses.clear();
        winners.clear();
        losers.clear();
        secondsPassed.set(0);
        for (WebSocketSession webSocketSession : webSocketSessions.values()) {
            sendMessage(webSocketSession, new TextMessage("Game started! You have 10 seconds to give your guess!"));
        }
        CompletableFuture.runAsync(this::runRound);
    }

    private void runRound() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                secondsPassed.addAndGet(1);
                // If game state change to not running, need to stop counting.
                if (!isRoundRunning.get()) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        finishRound();
    }

    public synchronized void finishRound() {
        if (!isRoundRunning.get()) {
            return;
        }
        log.info("Finishing round");
        isRoundRunning.set(false);
        secondsPassed.set(0);
        verifyGuesses();
        notifyUsers();
        startRound();
    }

    public void makeBet(WebSocketSession session, GuessPayload guess) {
        if (isRoundRunning.get()) {
            final var username = session.getAttributes().get("username").toString();

            guesses.computeIfAbsent(username, (e) -> {
                log.info("User {} made a bet on {}", username, guess);
                sendMessage(session, new TextMessage("Your guess is " + guess.guess() + " you bet " + guess.bet()));
                return guess;
            });
//            if (guesses.size() >= webSocketSessions.size()) {
//                finishRound();
//            }
        }
    }

    private void verifyGuesses() {
        int winnerNumber = calculateWinnerNumber();
        guesses.forEach((username, guess) -> {
            if (guess.guess().equals(winnerNumber)) {
                final var winner = new WinnerPayload(username, guess.guess(), guess.bet(), guess.bet().multiply(STAKE_RATIO));
                winners.add(winner);
                log.info("User {} won!", username);
            } else {
                losers.add(username);
                log.info("User {} Lost!", username);
            }
        });
    }

    private void notifyUsers() {
        for (WinnerPayload winner : winners) {
            final var session = webSocketSessions.get(winner.username());
            if (session != null) {
                sendMessage(session, new TextMessage("You won " + winner.winnings()));
            }
        }
        for (String username : losers) {
            final var session = webSocketSessions.get(username);
            if (session != null) {
                sendMessage(session, new TextMessage("You lost, don't be sad try one more time!"));
            }
        }
        for (WebSocketSession session : webSocketSessions.values()) {
            final var winnerText = new TextMessage("The winners are " + winners);
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

    private int calculateWinnerNumber() {
        return random.nextInt(MAX_GUESS - MIN_GUESS + 1) + MIN_GUESS;
    }


}
