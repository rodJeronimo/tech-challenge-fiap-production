package com.fiap.techchallenge.production.gateway.port;

import com.fiap.techchallenge.production.presentation.dtos.ErrorDeadLetterDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

public interface AsynchronousRequestPort {

    void updateOrderStatus(OrderUpdateStatusRequestDto orderUpdateStatusResquestDto);

    <T> void sendStatusDl(ErrorDeadLetterDto<T> deadLetterDto);

    <T> void sendPaymentCompletedDl(ErrorDeadLetterDto<T> deadLetterDto);
}
