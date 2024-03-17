package com.fiap.techchallenge.production.application.useCases;

import org.springframework.stereotype.Service;

import com.fiap.techchallenge.production.application.services.OrderService;
import com.fiap.techchallenge.production.domain.Order;
import com.fiap.techchallenge.production.domain.Status;
import com.fiap.techchallenge.production.domain.exceptions.BadRequestException;
import com.fiap.techchallenge.production.domain.exceptions.BaseHttpException.RequestDataDto;
import com.fiap.techchallenge.production.domain.exceptions.InternalServerErrorException;
import com.fiap.techchallenge.production.domain.exceptions.InvalidDataException;
import com.fiap.techchallenge.production.domain.exceptions.NotFoundException;
import com.fiap.techchallenge.production.domain.exceptions.ResourceNotFoundException;
import com.fiap.techchallenge.production.presentation.dtos.OrderDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderUseCases {

    private final OrderService orderService;

    public OrderDto updateStatus(OrderUpdateStatusRequestDto updateStatusResquest) {
        try {
            Order order = orderService.updateStatus(updateStatusResquest.getOrderId(),
                    new Status(updateStatusResquest.getStatus()));
            return OrderDto.of(order);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage(), new RequestDataDto(updateStatusResquest));

        } catch (InvalidDataException e) {
            throw new BadRequestException(e.getMessage(), new RequestDataDto(updateStatusResquest));

        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), new RequestDataDto(updateStatusResquest));
        }
    }

    public OrderDto createStatusInCache(OrderUpdateStatusRequestDto updateStatusResquest) {
        try {
            Order order = orderService.createStatusInCache(updateStatusResquest.getOrderId(),
                    new Status(updateStatusResquest.getStatus()));
            return OrderDto.of(order);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage(), new RequestDataDto(updateStatusResquest));

        } catch (InvalidDataException e) {
            throw new BadRequestException(e.getMessage(), new RequestDataDto(updateStatusResquest));

        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), new RequestDataDto(updateStatusResquest));
        }
    }

}
