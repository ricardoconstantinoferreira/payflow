package com.flow.payflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fees")
public class Fees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            referencedColumnName = "id"
    )
    private Store store;

    @ManyToOne
    @JoinColumn(
            name = "transaction_id",
            referencedColumnName = "id"
    )
    private Transaction transaction;

    public Fees() {
    }

    public Fees(Long id, String description, Store store, Transaction transaction) {
        this.id = id;
        this.description = description;
        this.store = store;
        this.transaction = transaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
