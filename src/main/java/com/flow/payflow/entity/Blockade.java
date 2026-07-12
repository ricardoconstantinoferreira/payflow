package com.flow.payflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "blockade")
public class Blockade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qty")
    private Long qty;

    @Column(name = "type_parameter")
    private TypeParameter typeParameter;

    @Column(name = "parameter")
    private Long parameter;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            referencedColumnName = "id"
    )
    private Store store;

    public Blockade() {
    }

    public Blockade(Long id, Long qty, Long interval,
                    TypeParameter typeParameter, Long parameter, Store store) {
        this.id = id;
        this.qty = qty;
        this.typeParameter = typeParameter;
        this.parameter = parameter;
        this.store = store;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public TypeParameter getTypeParameter() {
        return typeParameter;
    }

    public void setTypeParameter(TypeParameter typeParameter) {
        this.typeParameter = typeParameter;
    }

    public Long getParameter() {
        return parameter;
    }

    public void setParameter(Long parameter) {
        this.parameter = parameter;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
