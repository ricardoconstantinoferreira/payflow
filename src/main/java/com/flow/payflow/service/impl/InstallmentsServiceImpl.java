package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Fees;
import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.entity.Store;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.service.FeesConfigService;
import com.flow.payflow.service.FeesService;
import com.flow.payflow.service.InstallmentsService;
import com.flow.payflow.service.StoreService;
import org.springframework.stereotype.Service;

@Service
public class InstallmentsServiceImpl implements InstallmentsService {

    private final StoreService storeService;
    private final FeesConfigService feesConfigService;

    public InstallmentsServiceImpl(
            StoreService storeService,
            FeesConfigService feesConfigService
    ) {
        this.storeService = storeService;
        this.feesConfigService = feesConfigService;
    }

    @Override
    public Float getCalcAmountTotal(Transaction transaction, String token) {
        Float amountTotal = 0f;

        Store store = storeService.getStoreByToken(token);
        FeesConfig feesConfig = feesConfigService.getByStoreId(store.getId());

        int feesValue = feesConfig.getFees();
        int installments = transaction.getInstallments();
        int installmentsConfig = feesConfig.getInstallments();
        Float amount = transaction.getAmount();

        if (installments >= installmentsConfig) {
            amountTotal = (float) (amount * (Double.parseDouble(String.valueOf(feesValue)) / 100) + amount);
        } else {
            amountTotal = amount;
        }

        return amountTotal;
    }
}
