package com.flow.payflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Country(
        String numeric,
        String alpha2,
        String name,
        String emoji,
        String currency,
        Long latitude,
        Long longitude
) {}
