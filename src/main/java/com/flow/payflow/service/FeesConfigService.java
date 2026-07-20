package com.flow.payflow.service;

import com.flow.payflow.entity.FeesConfig;

public interface FeesConfigService {

    FeesConfig save(FeesConfig feesConfig, Long storeId);
    FeesConfig getByStoreByToken(String token);
    FeesConfig getByStoreId(Long storeId);
    void deleteById(Long id);
    FeesConfig getById(Long id);
}
