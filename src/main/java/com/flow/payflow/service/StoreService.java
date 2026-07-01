package com.flow.payflow.service;

import com.flow.payflow.entity.Store;

import java.util.List;

public interface StoreService {

    Store create(Store store);

    Store getById(Long id);

    List<Store> getAll();

    void deleteById(Long id);

    void saveToken(Store store, String token);

    Store getStoreByEmail(String email);
}
