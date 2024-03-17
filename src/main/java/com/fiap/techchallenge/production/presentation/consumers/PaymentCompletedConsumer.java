package com.fiap.techchallenge.production.presentation.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.production.application.enums.StatusEnum;
import com.fiap.techchallenge.production.application.useCases.OrderUseCases;
import com.fiap.techchallenge.production.domain.exceptions.BaseHttpException;
import com.fiap.techchallenge.production.gateway.port.AsynchronousRequestPort;
import com.fiap.techchallenge.production.presentation.dtos.ErrorDeadLetterDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderPaymentConsumerDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedConsumer {

    private final ObjectMapper objectMapper;
    private final OrderUseCases orderUseCases;
    private final AsynchronousRequestPort asynchronousRequestPort;

    @KafkaListener(topics = "${kafka.topic.payment-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload String message) throws JsonProcessingException {
        try {
            OrderPaymentConsumerDto orderPaymentUpdate = objectMapper.readValue(message, OrderPaymentConsumerDto.class);

            OrderUpdateStatusRequestDto orderUpdateStatusResquestDto = OrderUpdateStatusRequestDto.builder()
                    .orderId(orderPaymentUpdate.getOrderId())
                    .status(StatusEnum.ORDER_RECEIVED.name().toLowerCase())
                    .build();
            orderUseCases.createStatusInCache(orderUpdateStatusResquestDto);

        } catch (BaseHttpException e) {
            ErrorDeadLetterDto<Object> errorConsumer = ErrorDeadLetterDto.builder()
                    .errorCode(e.getStatusCode().value())
                    .errorDetail(e.getMessage())
                    .rawData(message)
                    .build();
            asynchronousRequestPort.sendPaymentCompletedDl(errorConsumer);
        } catch (Exception e) {
            log.error("Erro processing the message {} on the {}: {} ", message, this.getClass().getSimpleName(),
                    e.getMessage());
        }
    }

}
