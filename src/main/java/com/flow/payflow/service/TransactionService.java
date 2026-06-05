package com.flow.payflow.service;

import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse create(TransactionDto request);

    TransactionResponse getById(Long id);

    Page<TransactionResponse> list(Pageable pageable);
}
