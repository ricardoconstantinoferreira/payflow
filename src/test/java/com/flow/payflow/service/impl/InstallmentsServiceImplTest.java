package com.flow.payflow.service.impl;

import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.entity.Store;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.service.FeesConfigService;
import com.flow.payflow.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentsServiceImplTest {

    @Mock
    private StoreService storeService;

    @Mock
    private FeesConfigService feesConfigService;

    @InjectMocks
    private InstallmentsServiceImpl service;

    @Test
    void whenInstallmentsLessThanConfigNoInterest() {
        Store store = new Store();
        store.setId(1L);
        when(storeService.getStoreByToken("t")).thenReturn(store);

        FeesConfig cfg = new FeesConfig();
        cfg.setFees(10);
        cfg.setInstallments(3);
        when(feesConfigService.getByStoreId(1L)).thenReturn(cfg);

        Transaction tx = new Transaction();
        tx.setInstallments(2);
        tx.setAmount(100f);

        Float result = service.getCalcAmountTotal(tx, "t");

        assertEquals(100f, result);
    }
}
