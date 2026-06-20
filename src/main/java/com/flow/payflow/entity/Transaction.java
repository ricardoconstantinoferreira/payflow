package com.flow.payflow.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String orderId;

    @Column(nullable = false, length = 255)
    private Float amount;

    @Column(nullable = false, length = 255)
    private String currency;

    @Column(nullable = false, length = 11)
    private int installments;

    @Column(nullable = false, length = 255)
    private String paymentMethod;

    @Column(nullable = false, length = 255)
    private String cardToken;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id"
    )
    private Customer customer;

    @ManyToOne
    @JoinColumn(
            name = "billing_address_id",
            referencedColumnName = "id"
    )
    private BillingAddress billingAddress;

    public Transaction() {}

    public Transaction(Long id, String orderId, Float amount, String currency, int installments,
                       String paymentMethod, String cardToken, Customer customer, BillingAddress billingAddress) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.installments = installments;
        this.paymentMethod = paymentMethod;
        this.cardToken = cardToken;
        this.customer = customer;
        this.billingAddress = billingAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
