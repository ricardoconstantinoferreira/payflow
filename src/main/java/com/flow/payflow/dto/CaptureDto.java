package com.flow.payflow.dto;

import jakarta.validation.constraints.NotBlank;

public class CaptureDto {

    @NotBlank(message = "Amount não deve ser vazio.")
    private Float amount;

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
