package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import com.flow.payflow.service.StoreService;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store create(Store store) {
        return storeRepository.save(store);
    }
}
