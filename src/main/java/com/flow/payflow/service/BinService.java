package com.flow.payflow.service;

import com.flow.payflow.entity.BinListResponse;

public interface BinService {

    BinListResponse getCardData(String cardNumber);
}
