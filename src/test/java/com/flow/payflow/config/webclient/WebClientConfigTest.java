package com.flow.payflow.config.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class WebClientConfigTest {

    private final WebClientConfig config = new WebClientConfig();

    @Test
    void webClientBuilder_shouldReturnBuilder() {
        WebClient.Builder builder = config.webClientBuilder();
        assertNotNull(builder);
    }
}
