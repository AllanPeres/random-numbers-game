package com.allanperes.randomnumbersgame.utils;

import com.allanperes.randomnumbersgame.models.User;
import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import com.allanperes.randomnumbersgame.models.dto.WinningsDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameData {

    @Getter
    private final ConcurrentHashMap<String, GuessDto> guesses;
    @Getter
    private final List<WinningsDto> winners;
    @Getter
    private final List<String> losers;
    @Getter
    private final List<User> users;

    public GameData() {
        this.guesses = new ConcurrentHashMap<>();
        this.winners = new CopyOnWriteArrayList<>();
        this.losers = new CopyOnWriteArrayList<>();
        this.users = new CopyOnWriteArrayList<>();
    }

    public void addPlayer(User user) {
        this.users.add(user);
    }

    public void removePlayer(User user) {
        this.users.remove(user);
    }

    public void addGuess(String username, Function<String, GuessDto> mappingFunction) {
        guesses.computeIfAbsent(username, mappingFunction);
    }

    public void calculateWinnersAndLosers(Predicate<Integer> winningFunction, Function<BigDecimal, BigDecimal> winningsFunction) {
        for (Map.Entry<String, GuessDto> keyValue : guesses.entrySet()) {
            var guess = keyValue.getValue();
            var username = keyValue.getKey();
            if (winningFunction.test(guess.guess())) {
                final var winner = new WinningsDto(username, guess.guess(), guess.bet(), winningsFunction.apply(guess.bet()));
                winners.add(winner);
                log.info("User {} won!", username);
            } else {
                losers.add(username);
                log.info("User {} Lost!", username);
            }
        }
    }

    public void reset() {
        guesses.clear();
        winners.clear();
        losers.clear();
    }



}
