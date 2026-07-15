package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.dto.TransactionStatusResponseDto;
import com.flow.payflow.exception.MessageException;
import com.flow.payflow.service.ChangeStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChangeStatusServiceImpl implements ChangeStatusService {

    private static final Logger log = LoggerFactory.getLogger(ChangeStatusServiceImpl.class);
    private final WebClient webClient;

    public ChangeStatusServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public TransactionStatusResponseDto send(TransactionStatusDto dto, String uri) {
        try {
            log.info("Enviando a transação");
            return this.webClient.post()
                    .uri(uri)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(TransactionStatusResponseDto.class)
                    .block();
        } catch (Exception e) {
            log.error("Erro ao enviar a transação {}", e.getMessage());
            throw new MessageException("Error_Transaction", "Erro ao enviar a transação");
        }
    }
}
