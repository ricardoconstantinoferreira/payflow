package com.flow.payflow.service.impl;

import com.flow.payflow.dto.AutorizationDto;
import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.service.AutorizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AutorizationServiceImpl implements AutorizationService {

    private final WebClient webClient;

    @Value("${spring.uri.autorization}")
    private String uri;

    public AutorizationServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public AutorizationResponseDto autorization(TransactionDto dto, Float amountTotal) {

        AutorizationDto autorizationDto = new AutorizationDto();
        autorizationDto.setToken(dto.getCardToken());
        autorizationDto.setAmount(amountTotal);

        return this.webClient.post()
                .uri(uri)
                .bodyValue(autorizationDto)
                .retrieve()
                .bodyToMono(AutorizationResponseDto.class)
                .block();
    }
}
