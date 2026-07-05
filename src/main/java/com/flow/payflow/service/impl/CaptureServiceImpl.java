package com.flow.payflow.service.impl;

import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureResponseDto;
import com.flow.payflow.service.CaptureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CaptureServiceImpl implements CaptureService {

    private final WebClient webClient;

    @Value("${spring.uri.capture}")
    private String uri;

    public CaptureServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public CaptureResponseDto capture(CaptureApiDto dto) {

        return this.webClient.post()
                .uri(uri)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(CaptureResponseDto.class)
                .block();
    }
}
