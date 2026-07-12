package com.flow.payflow.service;

import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.dto.TransactionStatusResponseDto;

public interface ChangeStatusService {
    TransactionStatusResponseDto send(TransactionStatusDto dto, String uri);
}
