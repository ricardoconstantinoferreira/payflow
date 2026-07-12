package com.flow.payflow.dto;

public class FeesConfigDto {

    private Long id;

    private int installments;

    private int fees;

    private Long storeId;

    private Float minimalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Float getMinimalAmount() {
        return minimalAmount;
    }

    public void setMinimalAmount(Float minimalAmount) {
        this.minimalAmount = minimalAmount;
    }
}
