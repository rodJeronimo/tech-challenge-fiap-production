package com.fiap.techchallenge.production.presentation.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Builder.Default;

@Getter
@Builder
public class ErrorDeadLetterDto<T> {

    @Default
    private final String errorSource = "tech-challenge-fiap-production";
    private final Integer errorCode;
    private final String errorDetail;
    private final T rawData;

}
