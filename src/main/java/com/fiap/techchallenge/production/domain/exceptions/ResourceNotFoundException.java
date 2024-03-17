package com.fiap.techchallenge.production.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseHttpException {

    public ResourceNotFoundException(String message, RequestDataDto requestData) {
        super(HttpStatus.NOT_FOUND, message, requestData);
    }
}
