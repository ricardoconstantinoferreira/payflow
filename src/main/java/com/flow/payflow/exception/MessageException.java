package com.flow.payflow.exception;

public class MessageException extends RuntimeException {
    private String status;

    public MessageException(String message, String status) {
        super(message);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
