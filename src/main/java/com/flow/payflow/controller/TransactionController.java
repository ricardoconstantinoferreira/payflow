package com.flow.payflow.controller;

import com.flow.payflow.annotation.Idempotence;
import com.flow.payflow.config.rabbitmq.AutorizationMQConfig;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.entity.Status;
import com.flow.payflow.entity.Store;
import com.flow.payflow.service.ChangeStatusService;
import com.flow.payflow.service.StoreService;
import com.flow.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/transaction/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    private final RabbitTemplate rabbitTemplate;

    private final ChangeStatusService changeStatusService;

    private final StoreService storeService;

    @Autowired
    public TransactionController(
            TransactionService transactionService,
            RabbitTemplate rabbitTemplate,
            ChangeStatusService changeStatusService,
            StoreService storeService
    ) {
        this.transactionService = transactionService;
        this.rabbitTemplate = rabbitTemplate;
        this.changeStatusService = changeStatusService;
        this.storeService = storeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Idempotence(timeout = 30)
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody TransactionDto dto,
                                                      @RequestHeader("Authorization") String authorization) {

        if (authorization != null) {
            String token = authorization.replace("Bearer ", "");
            dto.setAuthToken(token);
        }

        log.info("Realizando a transação");
        log.info("Enviando a transação para o RabbitMQ");

        rabbitTemplate.convertAndSend(
                AutorizationMQConfig.EXCHANGE_PAYMENT,
                AutorizationMQConfig.ROUTING_KEY_PAYMENT,
                dto
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "processing", "message", "Transação enviada para processamento"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        TransactionResponse resp = transactionService.getById(id);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Map<String, String>> updateStatus(@PathVariable(value = "id") Long id,
                                                            @RequestBody TransactionStatusDto dto,
                                                            @RequestHeader("Authorization") String authorization) {

        String token = null;
        if (authorization != null) {
            token = authorization.replace("Bearer ", "");
        }

        Status status = Status.valueOf(dto.getStatus().toUpperCase());
        transactionService.updateStatus(id, status);
        log.info("Update status transaction to {}", status);

        Store store = storeService.getStoreByToken(token);
        changeStatusService.send(dto, store.getWebhook());
        log.info("Send status para webwook");

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "processing", "message", "Status de transação atualizada com sucesso."));
    }
}
