package com.flow.payflow.service;

import com.flow.payflow.dto.ValidateDto;
import com.flow.payflow.entity.BinListResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ValidateService {

    BinListResponse getBrand(String cardNumber);

    String getToken(ValidateDto validateDto) throws NoSuchAlgorithmException, InvalidKeyException;
}
