package com.fiap.techchallenge.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.techchallenge.production.application.enums.StatusEnum;
import com.fiap.techchallenge.production.util.StatusValidatorUtil;

public record Status(String value) {

    public Status {
        StatusValidatorUtil.validate(value);
    }

    public Boolean newStatusIsValid(Status newStatus) {
        StatusEnum actualStatusEnum = StatusEnum.valueOfIgnoreCase(value);
        StatusEnum newStatusEnum = StatusEnum.valueOfIgnoreCase(newStatus.value);

        return newStatusEnum.getPermittedBeforeStatus().contains(actualStatusEnum);

    }
}
