package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;
import com.flow.payflow.service.TokenrizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TokenrizationServiceImpl implements TokenrizationService {

    private static final Logger log = LoggerFactory.getLogger(TokenrizationServiceImpl.class);
    private final WebClient webClient;

    @Value("${spring.uri.tokenrization}")
    private String uri;

    public TokenrizationServiceImpl() {
        this.webClient = WebClient.create();
    }

    public TokenrizationResponseDto getTokenrization(TokenrizationDto tokenrizationDto) {
        log.info("Fazendo requisicao na api de tokenrizacao.");
        return this.webClient.post()
                .uri(uri)
                .bodyValue(tokenrizationDto)
                .retrieve()
                .bodyToMono(TokenrizationResponseDto.class)
                .block();
    }
}
