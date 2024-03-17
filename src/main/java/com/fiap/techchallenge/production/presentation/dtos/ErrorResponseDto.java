package com.fiap.techchallenge.production.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ErrorResponseDto(
        @JsonUnwrapped @JsonInclude(JsonInclude.Include.NON_NULL) Object requestData,
        String message) {
}
