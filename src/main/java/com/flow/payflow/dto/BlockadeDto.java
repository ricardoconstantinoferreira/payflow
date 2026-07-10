package com.flow.payflow.dto;

import com.flow.payflow.entity.TypeParameter;

public class BlockadeDto {

    private Long qty;

    private TypeParameter typeParameter;

    private Long parameter;

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
}
