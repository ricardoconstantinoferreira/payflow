package com.flow.payflow.config.component;

import com.flow.payflow.config.rabbitmq.AutorizationMQConfig;
import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.service.AutorizationService;
import com.flow.payflow.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AutorizationConsumer {

    private static final Logger log = LoggerFactory.getLogger(AutorizationConsumer.class);
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
            log.info("Pegando dados da fila e definindo o status para AUTHORIZED.");
            transactionService.updateStatus(response.id(), Status.AUTHORIZED);
        } else {
            log.error("Pegando dados da fila definindo o status para FAILED.");
            transactionService.updateStatus(response.id(), Status.FAILED);
        }
    }
}
