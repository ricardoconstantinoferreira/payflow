package com.flow.payflow.controller;

import com.flow.payflow.config.rabbitmq.CaptureMQConfig;
import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/payflow/{token}/capture", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaptureController {

    private static final Logger log = LoggerFactory.getLogger(CaptureController.class);
    private final RabbitTemplate rabbitTemplate;

    public CaptureController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> capture(
            @PathVariable(value = "token") String token,
            @RequestBody CaptureDto dto
            ) {

        CaptureApiDto captureApiDto = new CaptureApiDto();
        captureApiDto.setAmount(dto.getAmount());
        captureApiDto.setToken(token);

        log.info("Adicionando dados da captura na fila (RabbitMQ)");

        rabbitTemplate.convertAndSend(
                CaptureMQConfig.EXCHANGE_CAPTURE,
                CaptureMQConfig.ROUTING_KEY_CAPTURE,
                captureApiDto
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "capture", "message", "Captura enviada para processamento"));
    }
}
