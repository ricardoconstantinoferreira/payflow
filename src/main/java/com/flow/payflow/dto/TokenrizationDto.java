package com.flow.payflow.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenrizationDto {

    @NotBlank(message = "Holder Name não pode ser vazio")
    private String holderName;
    @NotBlank(message = "Card Number não pode ser vazio")
    private String card;
    @NotBlank(message = "Data de vencimento não pode ser vazia")
    private String venc;
    @NotBlank(message = "CVV não pode ser vazio")
    private String cvv;

    public @NotBlank(message = "Holder Name não pode ser vazio") String getHolderName() {
        return holderName;
    }

    public void setHolderName(@NotBlank(message = "Holder Name não pode ser vazio") String holderName) {
        this.holderName = holderName;
    }

    public @NotBlank(message = "Card Number não pode ser vazio") String getCard() {
        return card;
    }

    public void setCard(@NotBlank(message = "Card Number não pode ser vazio") String card) {
        this.card = card;
    }

    public @NotBlank(message = "Data de vencimento não pode ser vazia") String getVenc() {
        return venc;
    }

    public void setVenc(@NotBlank(message = "Data de vencimento não pode ser vazia") String venc) {
        this.venc = venc;
    }

    public @NotBlank(message = "CVV não pode ser vazio") String getCvv() {
        return cvv;
    }

    public void setCvv(@NotBlank(message = "CVV não pode ser vazio") String cvv) {
        this.cvv = cvv;
    }
}
