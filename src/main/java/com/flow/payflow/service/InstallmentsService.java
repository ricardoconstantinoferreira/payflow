package com.flow.payflow.service;

import com.flow.payflow.entity.Transaction;

public interface InstallmentsService {
    Float getCalcAmountTotal(Transaction transaction, String token);
}
