package com.flow.payflow.config.component;

import com.flow.payflow.config.rabbitmq.CaptureMQConfig;
import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureResponseDto;
import com.flow.payflow.entity.Status;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.service.CaptureService;
import com.flow.payflow.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CaptureConsumer {

    private final CaptureService captureService;
    private final TransactionService transactionService;

    public CaptureConsumer(
            CaptureService captureService,
            TransactionService transactionService
    ) {
        this.captureService = captureService;
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = CaptureMQConfig.QUEUE_CAPTURE)
    public void consumeCaptureMessage(CaptureApiDto dto) {
        CaptureResponseDto responseDto = captureService.capture(dto);

        if (!responseDto.getTransactionId().isEmpty()) {
            Transaction transaction = transactionService.getByCardToken(dto.getToken());
            transactionService.updateStatus(transaction.getId(), Status.CAPTURED);
        }
    }
}
