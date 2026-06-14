package com.flow.payflow.service.impl;

import com.flow.payflow.entity.BinListResponse;
import com.flow.payflow.service.BinService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BinServiceImpl implements BinService {

    private final WebClient webClient;

    @Value("${spring.uri.binlist.net}")
    private String uri;

    public BinServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Override
    public BinListResponse getCardData(String cardNumber) {
        String bin = cardNumber.substring(0, 8);
        String fullUri = uri + "/" + bin;
        try {
            return this.webClient.get()
                    .uri(fullUri)
                    .retrieve()
                    .bodyToMono(BinListResponse.class)
                    .block();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
