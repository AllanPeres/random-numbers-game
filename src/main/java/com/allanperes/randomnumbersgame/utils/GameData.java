package com.allanperes.randomnumbersgame.utils;

import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import com.allanperes.randomnumbersgame.models.dto.WinningsDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameData {

    @Getter
    private final ConcurrentHashMap<String, GuessDto> guesses;
    @Getter
    private final List<WinningsDto> winners;
    @Getter
    private final List<String> losers;

    public GameData() {
        this.guesses = new ConcurrentHashMap<>();
        this.winners = new CopyOnWriteArrayList<>();
        this.losers = new CopyOnWriteArrayList<>();
    }

    public void addGuess(String username, Function<String, GuessDto> mappingFunction) {
        guesses.computeIfAbsent(username, mappingFunction);
    }

    public void calculateWinnersAndLosers(Predicate<Integer> winningFunction, Function<BigDecimal, BigDecimal> winningsFunction) {
        guesses.forEach((username, guess) -> {
            if (winningFunction.test(guess.guess())) {
                final var winner = new WinningsDto(username, guess.guess(), guess.bet(), winningsFunction.apply(guess.bet()));
                winners.add(winner);
                log.info("User {} won!", username);
            } else {
                losers.add(username);
                log.info("User {} Lost!", username);
            }
        });
    }

    public void reset() {
        guesses.clear();
        winners.clear();
        losers.clear();
    }



}
