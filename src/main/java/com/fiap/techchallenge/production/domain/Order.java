package com.fiap.techchallenge.production.domain;

import lombok.Builder;

public record Order(Long id, Status status) {

    @Builder
    public Order(Long id, String status) {
        this(id, new Status(status));
    }
}
