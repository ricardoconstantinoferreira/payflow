package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.repository.TransactionRepository;
import com.flow.payflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public TransactionResponse create(TransactionDto request) {
        Transaction t = new Transaction();
        t.setName(request.name());
        t.setDateValid(request.date_valid());
        t.setDigits(request.digits());
        t.setToken(request.token());
        t.setCreatedAt(OffsetDateTime.now());
        t.setValue(request.value());
        t.setStatus(Status.PENDING);
        Transaction saved = repository.save(t);
        return new TransactionResponse(saved.getId(), saved.getName(), saved.getDateValid(), saved.getDigits(), saved.getToken(), saved.getCreatedAt());
    }

    @Override
    public TransactionResponse getById(Long id) {
        Transaction t = repository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
        return new TransactionResponse(t.getId(), t.getName(), t.getDateValid(), t.getDigits(), t.getToken(), t.getCreatedAt());
    }

    @Override
    public Page<TransactionResponse> list(Pageable pageable) {
        Page<Transaction> page = repository.findAll(pageable);
        List<TransactionResponse> content = page.getContent().stream().map(t -> new TransactionResponse(t.getId(), t.getName(), t.getDateValid(), t.getDigits(), t.getToken(), t.getCreatedAt())).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }
}
