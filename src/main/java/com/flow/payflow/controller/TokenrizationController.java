package com.flow.payflow.controller;

import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;
import com.flow.payflow.service.TokenrizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/transaction/tokenrization", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenrizationController {

    @Autowired
    private TokenrizationService tokenrizationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenrizationResponseDto> validate(@Valid @RequestBody TokenrizationDto tokenrizationDto) {
        TokenrizationResponseDto responseDto =  tokenrizationService.getTokenrization(tokenrizationDto);
        return ResponseEntity.ok(responseDto);
    }
}
