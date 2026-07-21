package com.flow.payflow.service;

import com.flow.payflow.entity.Blockade;

public interface BlockadeService {
    Blockade save(Blockade blockade, String token);
    Blockade getByStoreId(Long storeId);
    Blockade getBlockadeConfig(String token);
    Blockade getById(Long id);
    void deleteById(Long id);
}
