package com.flow.payflow.controller;

import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;
import com.flow.payflow.exception.MessageException;
import com.flow.payflow.service.TokenrizationService;
import com.flow.payflow.usecase.VelocityCheck;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/transaction/tokenrization", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenrizationController {

    private static final Logger log = LoggerFactory.getLogger(TokenrizationController.class);
    @Autowired
    private TokenrizationService tokenrizationService;

    @Autowired
    private VelocityCheck velocityCheck;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenrizationResponseDto> validate(@Valid @RequestBody TokenrizationDto tokenrizationDto,
                                                             @RequestHeader("Authorization") String authorization) {

        String token = null;
        if (authorization != null) {
            token = authorization.replace("Bearer ", "");
            log.info("Recebendo o token pra tokenrizar cartao {}", token);
        }

        TokenrizationResponseDto responseDto = tokenrizationService.getTokenrization(tokenrizationDto);

        if (responseDto.getCode().equals("00")) {
            if (!velocityCheck.check(tokenrizationDto.getCard(), token)) {
                log.error("Quantidade de requisições ultrapassou o limite.");
                throw new MessageException("Message_Exceed", "Quantidade de requisições ultrapassou o limite.");
            }
        }

        return ResponseEntity.ok(responseDto);
    }
}
