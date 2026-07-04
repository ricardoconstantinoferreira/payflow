package com.flow.payflow.config.component;

import com.flow.payflow.config.rabbitmq.RabbitMQConfig;
import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.service.AutorizationService;
import com.flow.payflow.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    private final TransactionService transactionService;
    private final AutorizationService autorizationService;

    public PaymentConsumer(
            TransactionService transactionService,
            AutorizationService autorizationService
    ) {
        this.transactionService = transactionService;
        this.autorizationService = autorizationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT)
    public void consumePaymentMessage(TransactionDto transactionDto) {
        TransactionResponse response =  transactionService.create(transactionDto);
        AutorizationResponseDto responseDto = autorizationService.autorization(transactionDto, response.amountTotal());

        if (responseDto.getCode().equals("00")) {
            transactionService.updateStatus(transactionDto, response.id(), Status.AUTHORIZED);
        } else {
            transactionService.updateStatus(transactionDto, response.id(), Status.FAILED);
        }
    }
}
