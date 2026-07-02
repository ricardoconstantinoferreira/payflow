package com.flow.payflow.service.impl;

import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.repository.FeesConfigRepository;
import com.flow.payflow.service.FeesConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeesConfigServiceImpl implements FeesConfigService {

    private final FeesConfigRepository feesConfigRepository;

    public FeesConfigServiceImpl(FeesConfigRepository feesConfigRepository) {
        this.feesConfigRepository = feesConfigRepository;
    }

    @Override
    public FeesConfig save(FeesConfig feesConfig) {
        return feesConfigRepository.save(feesConfig);
    }

    @Override
    public FeesConfig getByStoreId(Long storeId) {
        Optional<FeesConfig> feesConfig = feesConfigRepository.findByStoreId(storeId);
        return (feesConfig.isEmpty()) ? null : feesConfig.get();
    }

    @Override
    public List<FeesConfig> getAll() {
        return feesConfigRepository.findAll();
    }
}
