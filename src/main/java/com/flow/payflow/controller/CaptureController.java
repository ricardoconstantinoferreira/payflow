package com.flow.payflow.controller;

import com.flow.payflow.config.rabbitmq.CaptureMQConfig;
import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureDto;
import com.flow.payflow.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/transaction/{token}/capture", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaptureController {

    private final RabbitTemplate rabbitTemplate;

    public CaptureController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> capture(
            @PathVariable(value = "token") String token,
            @Valid @RequestBody CaptureDto dto
            ) {

        CaptureApiDto captureApiDto = new CaptureApiDto();
        captureApiDto.setAmount(dto.getAmount());
        captureApiDto.setToken(token);

        rabbitTemplate.convertAndSend(
                CaptureMQConfig.EXCHANGE_CAPTURE,
                CaptureMQConfig.ROUTING_KEY_CAPTURE,
                captureApiDto
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "capture", "message", "Captura enviada para processamento"));
    }
}
