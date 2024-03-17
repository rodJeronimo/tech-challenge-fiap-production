package com.fiap.techchallenge.production.presentation.dtos;

import com.fiap.techchallenge.production.domain.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderUpdateStatusRequestDto {

    private long orderId;
    private String status;

    public static OrderUpdateStatusRequestDto of(Order order) {
        return OrderUpdateStatusRequestDto.builder()
                .orderId(order.id())
                .status(order.status().value())
                .build();
    }
}
