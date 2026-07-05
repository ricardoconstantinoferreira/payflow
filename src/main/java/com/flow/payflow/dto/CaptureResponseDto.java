package com.flow.payflow.dto;

import java.util.Date;

public class CaptureResponseDto {
    private String transactionId;
    private String status;
    private Float capturedAmount;
    private Date capturedAt;
    private String message;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(Float capturedAmount) {
        this.capturedAmount = capturedAmount;
    }

    public Date getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Date capturedAt) {
        this.capturedAt = capturedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
