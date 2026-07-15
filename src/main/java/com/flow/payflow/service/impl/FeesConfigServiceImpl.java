package com.flow.payflow.service.impl;

import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.FeesConfigRepository;
import com.flow.payflow.service.FeesConfigService;
import com.flow.payflow.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeesConfigServiceImpl implements FeesConfigService {

    private static final Logger log = LoggerFactory.getLogger(FeesConfigServiceImpl.class);
    private final FeesConfigRepository feesConfigRepository;
    private final StoreService storeService;

    public FeesConfigServiceImpl(FeesConfigRepository feesConfigRepository, StoreService storeService) {
        this.feesConfigRepository = feesConfigRepository;
        this.storeService = storeService;
    }

    @Override
    public FeesConfig save(FeesConfig feesConfig, Long storeId) {
        Store store = storeService.getById(storeId);

        if (store.getToken().isEmpty()) {
            log.error("Token invalid {}", store.getToken());
            throw new RuntimeException("Token invalid!");
        }

        feesConfig.setStore(store);

        if (hasFeesConfig(storeId) > 0) {
            feesConfig.setId(hasFeesConfig(storeId));
        }

        return feesConfigRepository.save(feesConfig);
    }

    @Override
    public FeesConfig getByStoreByToken(String token) {
        Store store = storeService.getStoreByToken(token);
        Optional<FeesConfig> feesConfig = feesConfigRepository.findByStoreId(store.getId());
        return (feesConfig.isEmpty()) ? null : feesConfig.get();
    }

    @Override
    public FeesConfig getByStoreId(Long storeId) {
        Optional<FeesConfig> feesConfig = feesConfigRepository.findByStoreId(storeId);
        return (feesConfig.isEmpty()) ? null : feesConfig.get();
    }

    private Long hasFeesConfig(Long storeId) {
        Optional<FeesConfig> feesConfig = feesConfigRepository.findByStoreId(storeId);
        return (feesConfig.isEmpty()) ? 0 : feesConfig.get().getId();
    }
}
