package com.flow.payflow.controller;

import com.flow.payflow.annotation.Idempotence;
import com.flow.payflow.config.rabbitmq.AutorizationMQConfig;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.entity.Status;
import com.flow.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/transaction/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TransactionController(TransactionService transactionService, RabbitTemplate rabbitTemplate) {
        this.transactionService = transactionService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Idempotence(timeout = 30)
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody TransactionDto dto,
                                                      @RequestHeader("Authorization") String authorization) {

        if (authorization != null) {
            String token = authorization.replace("Bearer ", "");
            dto.setAuthToken(token);
        }

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

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> list(Pageable pageable) {
        Page<TransactionResponse> page = transactionService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Map<String, String>> updateStatus(@PathVariable(value = "id") Long id,
                                                            @RequestBody TransactionStatusDto dto) {

        Status status = Status.valueOf(dto.getStatus().toUpperCase());
        transactionService.updateStatus(id, status);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "processing", "message", "Status de transação atualizada com sucesso."));
    }
}
