package com.fiap.techchallenge.production.application.services;

import com.fiap.techchallenge.production.domain.Order;
import com.fiap.techchallenge.production.domain.Status;

public interface OrderService {

    Order updateStatus(Long orderId, Status newStatus);

    Order createStatusInCache(Long orderId, Status newStatus);

}
