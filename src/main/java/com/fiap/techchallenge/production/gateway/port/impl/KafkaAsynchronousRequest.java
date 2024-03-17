package com.fiap.techchallenge.production.gateway.port.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.production.gateway.port.AsynchronousRequestPort;
import com.fiap.techchallenge.production.presentation.dtos.ErrorDeadLetterDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaAsynchronousRequest implements AsynchronousRequestPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.order-status}")
    private String orderStatusTopic;

    @Value("${kafka.topic.order-status.dl}")
    private String orderStatusDlTopic;

    @Value("${kafka.topic.payment-completed.dl}")
    private String paymentCompletedDlTopic;

    private <T> void send(String topicName, T objectToSend) {
        try {
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(objectToSend));
        } catch (Exception e) {
            log.error("Error:{}. While sending the message:{}. To the topic: {}", e.getMessage(), objectToSend,
                    topicName);
        }
    }

    @Override
    public void updateOrderStatus(OrderUpdateStatusRequestDto orderUpdateStatusResquestDto) {
        send(orderStatusTopic, orderUpdateStatusResquestDto);
    }

    @Override
    public <T> void sendStatusDl(ErrorDeadLetterDto<T> deadLetterDto) {
        send(orderStatusDlTopic, deadLetterDto);
    }

    @Override
    public <T> void sendPaymentCompletedDl(ErrorDeadLetterDto<T> deadLetterDto) {
        send(paymentCompletedDlTopic, deadLetterDto);
    }

}
