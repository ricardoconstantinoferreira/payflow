package com.flow.payflow.controller;

import com.flow.payflow.annotation.Idempotence;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/transaction/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Idempotence(timeout = 30)
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionDto dto, UriComponentsBuilder uriBuilder) {
        TransactionResponse created = transactionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).location(uriBuilder.path("/api/transaction/{id}").buildAndExpand(created.id()).toUri()).body(created);
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
}
