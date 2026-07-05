package com.flow.payflow.service;

import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse create(TransactionDto request);
    TransactionResponse getById(Long id);
    Page<TransactionResponse> list(Pageable pageable);
    void updateStatus(Long id, Status status);
    Transaction getByCardToken(String token);
}
