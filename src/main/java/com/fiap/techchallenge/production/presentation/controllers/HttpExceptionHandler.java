package com.fiap.techchallenge.production.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fiap.techchallenge.production.domain.exceptions.BaseHttpException;
import com.fiap.techchallenge.production.domain.exceptions.BaseHttpException.RequestDataDto;
import com.fiap.techchallenge.production.gateway.port.AsynchronousRequestPort;
import com.fiap.techchallenge.production.presentation.dtos.ErrorDeadLetterDto;
import com.fiap.techchallenge.production.presentation.dtos.ErrorResponseDto;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class HttpExceptionHandler {

    private final AsynchronousRequestPort asynchronousRequestPort;
    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpException(BaseHttpException e) {

        RequestDataDto requestDataDto = e.getRequestData();
        Object rawRequestData = requestDataDto != null ? requestDataDto.requestData() : null;

        ErrorDeadLetterDto<Object> errorConsumer = ErrorDeadLetterDto.builder()
                .errorCode(e.getStatusCode().value())
                .errorDetail(e.getMessage())
                .rawData(rawRequestData)
                .build();

        asynchronousRequestPort.sendStatusDl(errorConsumer);

        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorResponseDto(rawRequestData,
                        e.getMessage()));
    }

}
