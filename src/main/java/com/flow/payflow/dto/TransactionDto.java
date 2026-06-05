package com.flow.payflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TransactionDto(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 20) String date_valid,
        @NotBlank @Size(min = 4, max = 4) String digits,
        @NotBlank @Size(max = 255) String token
) {
}
