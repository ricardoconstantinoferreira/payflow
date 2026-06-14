package com.flow.payflow.service.impl;

import com.flow.payflow.dto.ValidateDto;
import com.flow.payflow.entity.BinListResponse;
import com.flow.payflow.service.BinService;
import com.flow.payflow.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ValidateServiceImpl implements ValidateService {

    @Autowired
    private BinService binService;

    @Value("${spring.data.secretkey.payflow}")
    private String keySecret;

    @Override
    public BinListResponse getBrand(String cardNumber) {
        return binService.getCardData(cardNumber);
    }

    @Override
    public String getToken(ValidateDto validateDto) throws NoSuchAlgorithmException, InvalidKeyException {
        String cardDate = validateDto.getCardNumber() + "|" + validateDto.getHolderName() + "|" +
                validateDto.getExpirationMonth() + "|" + validateDto.getExpirationYear() + "|" + validateDto.getCvv();

        Mac sha256MAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256MAC.init(secretKey);

        byte[] hashBytes = sha256MAC.doFinal(cardDate.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
