package com.flow.payflow.config.component;

import com.flow.payflow.config.rabbitmq.AutorizationMQConfig;
import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.service.AutorizationService;
import com.flow.payflow.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AutorizationConsumer {

    private final TransactionService transactionService;
    private final AutorizationService autorizationService;

    public AutorizationConsumer(
            TransactionService transactionService,
            AutorizationService autorizationService
    ) {
        this.transactionService = transactionService;
        this.autorizationService = autorizationService;
    }

    @RabbitListener(queues = AutorizationMQConfig.QUEUE_PAYMENT)
    public void consumeAutorizationMessage(TransactionDto transactionDto) {
        TransactionResponse response =  transactionService.create(transactionDto);
        AutorizationResponseDto responseDto = autorizationService.autorization(transactionDto, response.amountTotal());

        if (responseDto.getCode().equals("00")) {
            transactionService.updateStatus(response.id(), Status.AUTHORIZED);
        } else {
            transactionService.updateStatus(response.id(), Status.FAILED);
        }
    }
}
