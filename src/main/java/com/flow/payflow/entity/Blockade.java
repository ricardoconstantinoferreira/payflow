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

    @Column(name = "`interval`")
    private Long interval;

    @Column(name = "type_parameter")
    private TypeParameter typeParameter;

    @Column(name = "parameter")
    private Long parameter;

    public Blockade() {
    }

    public Blockade(Long id, Long qty, Long interval, TypeParameter typeParameter, Long parameter) {
        this.id = id;
        this.qty = qty;
        this.interval = interval;
        this.typeParameter = typeParameter;
        this.parameter = parameter;
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

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
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
}
