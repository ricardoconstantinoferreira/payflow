package com.flow.payflow.dto;

public record TransactionResponse(
        Long id,
        String orderId,
        Float amount,
        String currency,
        int installments,
        String paymentMethod,
        String cardToken
) {
}
