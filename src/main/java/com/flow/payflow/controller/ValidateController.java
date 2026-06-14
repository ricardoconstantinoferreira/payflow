package com.flow.payflow.controller;

import com.flow.payflow.dto.ValidateDto;
import com.flow.payflow.dto.ValidateResponseDto;
import com.flow.payflow.entity.BinListResponse;
import com.flow.payflow.service.ValidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/v1/transaction/validate", produces = MediaType.APPLICATION_JSON_VALUE)
public class ValidateController {

    @Autowired
    private ValidateService validateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidateResponseDto> validate(@Valid @RequestBody ValidateDto validateDto) throws NoSuchAlgorithmException, InvalidKeyException {
        BinListResponse binListResponse = validateService.getBrand(validateDto.getCardNumber());
        String token = validateService.getToken(validateDto);

        String cardNumber = validateDto.getCardNumber();

        ValidateResponseDto responseDto = new ValidateResponseDto();
        responseDto.setBrand(binListResponse.brand());
        responseDto.setCardToken(token);
        responseDto.setExpirationMonth(validateDto.getExpirationMonth());
        responseDto.setLastFourDigits(cardNumber.substring(cardNumber.length() - 4));
        responseDto.setExpirationYear(validateDto.getExpirationYear());

        return ResponseEntity.ok(responseDto);
    }
}
