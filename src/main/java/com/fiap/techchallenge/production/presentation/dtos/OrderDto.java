package com.fiap.techchallenge.production.presentation.dtos;

import com.fiap.techchallenge.production.domain.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class OrderDto {

    private long orderId;
    private String status;

    public static OrderDto of(Order order) {
        return OrderDto.builder()
                .orderId(order.id())
                .status(order.status().value())
                .build();
    }
}
