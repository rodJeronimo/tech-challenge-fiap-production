package com.fiap.techchallenge.production.application.services.impl;

import static com.fiap.techchallenge.production.util.ConstantsUtil.ORDER_NOT_FOUND;
import static com.fiap.techchallenge.production.util.ConstantsUtil.ORDER_NOT_FOUND_IN_CHACHE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.production.application.enums.StatusEnum;
import com.fiap.techchallenge.production.application.services.OrderService;
import com.fiap.techchallenge.production.domain.Order;
import com.fiap.techchallenge.production.domain.Status;
import com.fiap.techchallenge.production.domain.exceptions.InvalidDataException;
import com.fiap.techchallenge.production.domain.exceptions.NotFoundException;
import com.fiap.techchallenge.production.gateway.entity.OrderEntity;
import com.fiap.techchallenge.production.gateway.port.AsynchronousRequestPort;
import com.fiap.techchallenge.production.gateway.port.CachePort;
import com.fiap.techchallenge.production.gateway.repository.OrderRepository;
import com.fiap.techchallenge.production.presentation.dtos.OrderDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AsynchronousRequestPort asynchronousRequestPort;
    private final CachePort cachePort;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    private static final String ORDER_PRODUCTION_CACHE_PREFIX_KEY = "orderProduction::";

    @Override
    public Order updateStatus(Long orderId, Status newStatus) {

        Object orderCacheObject = cachePort.getValueByKey(ORDER_PRODUCTION_CACHE_PREFIX_KEY + orderId);

        if (Objects.isNull(orderCacheObject)) {
            throw new NotFoundException(ORDER_NOT_FOUND_IN_CHACHE);
        }

        Order orderSaved = objectMapper.convertValue(orderCacheObject, Order.class);

        if (!orderSaved.status().newStatusIsValid(newStatus)) {
            throw new InvalidDataException(
                    "Status " + orderSaved.status().value() + " cannot be changed to " + newStatus.value());
        }

        Order orderUpdated = Order.builder()
                .id(orderId)
                .status(newStatus.value())
                .build();

        asynchronousRequestPort.updateOrderStatus(OrderUpdateStatusRequestDto.of(orderUpdated));

        setOrderOnCache(orderUpdated);

        return orderUpdated;
    }

    @Override
    public Order createStatusInCache(Long orderId, Status newStatus) {

        OrderEntity orderEntity = findOrderEntityById(orderId);

        Order orderSaved = orderEntity.toDomain();

        if (!orderSaved.status().newStatusIsValid(newStatus)) {
            throw new InvalidDataException(
                    "Status " + orderSaved.status().value() + " cannot be changed to " + newStatus.value());
        }

        orderEntity.setStatus(newStatus.value());

        Order orderUpdated = orderEntity.toDomain();

        asynchronousRequestPort.updateOrderStatus(OrderUpdateStatusRequestDto.of(orderUpdated));

        setOrderOnCache(orderUpdated);

        return orderUpdated;
    }

    private OrderEntity findOrderEntityById(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);

        if (orderEntity.isEmpty()) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }

        return orderEntity.get();
    }

    private void setOrderOnCache(Order resultOrder) {

        cachePort.setKeyWithoutExpirationTime(ORDER_PRODUCTION_CACHE_PREFIX_KEY + resultOrder.id(),
                resultOrder);

    }
}
