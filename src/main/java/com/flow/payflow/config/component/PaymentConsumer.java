package com.flow.payflow.config.component;

import com.flow.payflow.config.rabbitmq.RabbitMQConfig;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    private final TransactionService transactionService;

    public PaymentConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT)
    public void consumePaymentMessage(TransactionDto transactionDto) {
        this.transactionService.create(transactionDto);
    }
}
