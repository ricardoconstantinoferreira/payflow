package com.flow.payflow.service;

import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;

public interface AutorizationService {
    AutorizationResponseDto autorization(TransactionDto dto, Float amountTotal);
}
