package com.fiap.techchallenge.production.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends BaseHttpException {

    public InternalServerErrorException(String message, RequestDataDto requestData) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, requestData);
    }
}
