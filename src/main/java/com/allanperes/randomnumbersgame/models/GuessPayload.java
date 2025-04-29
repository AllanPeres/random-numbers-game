package com.allanperes.randomnumbersgame.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Range;

public record GuessPayload(@NotNull @Range(min = 1, max = 10) Integer guess, @NotNull @Min(value = 1) BigDecimal bet) {
}
