package com.allanperes.randomnumbersgame.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Range;

public record GuessDto(@NotNull @Range(min = 1, max = 10) Integer guess, @NotNull @Min(value = 1) BigDecimal bet) {
}
