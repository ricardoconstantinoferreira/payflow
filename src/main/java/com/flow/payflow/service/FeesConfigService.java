package com.flow.payflow.service;

import com.flow.payflow.entity.FeesConfig;

import java.util.List;

public interface FeesConfigService {

    FeesConfig save(FeesConfig feesConfig);
    FeesConfig getByStoreId(Long storeId);
    List<FeesConfig> getAll();
}
