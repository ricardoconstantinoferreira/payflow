package com.flow.payflow.dto;

import java.time.OffsetDateTime;

public record TransactionResponse(
        Long id,
        String name,
        String date_valid,
        String digits,
        String token,
        OffsetDateTime createdAt
) {
}
