package com.allanperes.randomnumbersgame.models;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record WinnerPayload(@NotNull String username, @NotNull Integer guess, @NotNull BigDecimal bet, @NotNull BigDecimal winnings) {
}
