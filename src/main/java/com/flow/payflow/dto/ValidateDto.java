package com.flow.payflow.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidateDto {

    @NotBlank(message = "Holder Name não pode ser vazio")
    private String holderName;
    @NotBlank(message = "Card Number não pode ser vazio")
    private String cardNumber;
    @NotBlank(message = "Expiration Month não pode ser vazio")
    private String expirationMonth;
    @NotBlank(message = "Expiration Year não pode ser vazio")
    private String expirationYear;
    @NotBlank(message = "CVV não pode ser vazio")
    private String cvv;

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
