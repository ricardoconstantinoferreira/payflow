package com.flow.payflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BinListResponse(
        String scheme,
        String type,
        String brand,
        Boolean prepaid,
        Country country,
        Bank bank
) {}

