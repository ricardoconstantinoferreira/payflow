package com.flow.payflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fees_config")
public class FeesConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "installments", nullable = false)
    private int installments;

    @Column(name = "fees", nullable = false)
    private int fees;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            referencedColumnName = "id"
    )
    private Store store;

    @Column(name = "minimal_amount", nullable = false)
    private Float minimalAmount;

    public FeesConfig() {
    }

    public FeesConfig(Long id, int installments, int fees, Store store, Float minimalAmount) {
        this.id = id;
        this.installments = installments;
        this.fees = fees;
        this.store = store;
        this.minimalAmount = minimalAmount;
    }

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Float getMinimalAmount() {
        return minimalAmount;
    }

    public void setMinimalAmount(Float minimalAmount) {
        this.minimalAmount = minimalAmount;
    }
}
