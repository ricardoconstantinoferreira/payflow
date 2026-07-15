package com.flow.payflow.service.impl;

import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureResponseDto;
import com.flow.payflow.service.CaptureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CaptureServiceImpl implements CaptureService {

    private static final Logger log = LoggerFactory.getLogger(CaptureServiceImpl.class);
    private final WebClient webClient;

    @Value("${spring.uri.capture}")
    private String uri;

    public CaptureServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public CaptureResponseDto capture(CaptureApiDto dto) {
        log.info("Enviando dados da captura para adquirente.");
        return this.webClient.post()
                .uri(uri)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(CaptureResponseDto.class)
                .block();
    }
}
