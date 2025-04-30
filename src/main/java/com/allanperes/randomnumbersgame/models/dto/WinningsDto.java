package com.allanperes.randomnumbersgame.models.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record WinningsDto(@NotNull String username, @NotNull Integer guess, @NotNull BigDecimal bet, @NotNull BigDecimal winnings) {

    public String getWinningsLog() {
        return "User: " + username + " Won: " + winnings;
    }
}
