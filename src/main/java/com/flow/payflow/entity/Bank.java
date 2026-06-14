package com.flow.payflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Bank(
        String name,
        String url,
        String phone,
        String city
) {}
