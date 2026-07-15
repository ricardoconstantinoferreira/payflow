package com.flow.payflow.service.impl;

import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.entity.Store;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.service.FeesConfigService;
import com.flow.payflow.service.InstallmentsService;
import com.flow.payflow.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InstallmentsServiceImpl implements InstallmentsService {

    private static final Logger log = LoggerFactory.getLogger(InstallmentsServiceImpl.class);
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
            log.info("Parcelado, entao calcula os juros para o total");
            amountTotal = (float) (amount * (Double.parseDouble(String.valueOf(feesValue)) / 100) + amount);
        } else {
            log.info("Não parcelado, nao calcula juros");
            amountTotal = amount;
        }

        return amountTotal;
    }
}
