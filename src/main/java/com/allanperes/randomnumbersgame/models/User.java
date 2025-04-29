package com.allanperes.randomnumbersgame.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record User(@NotNull @NotEmpty String sessionId, @NotNull @NotEmpty String username) {
}
