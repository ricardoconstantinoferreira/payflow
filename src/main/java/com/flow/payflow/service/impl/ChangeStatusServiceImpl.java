package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.dto.TransactionStatusResponseDto;
import com.flow.payflow.service.ChangeStatusService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChangeStatusServiceImpl implements ChangeStatusService {

    private final WebClient webClient;

    public ChangeStatusServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public TransactionStatusResponseDto send(TransactionStatusDto dto, String uri) {
        return this.webClient.post()
                .uri(uri)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(TransactionStatusResponseDto.class)
                .block();
    }
}
