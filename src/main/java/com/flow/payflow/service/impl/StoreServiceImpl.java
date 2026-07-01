package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import com.flow.payflow.service.StoreService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    public StoreServiceImpl(StoreRepository storeRepository, PasswordEncoder passwordEncoder) {
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Store create(Store store) {
        String password = passwordEncoder.encode(store.getPassword());
        store.setPassword(password);
        return storeRepository.save(store);
    }

    @Override
    public Store getById(Long id) {
        return storeRepository.getReferenceById(id);
    }

    @Override
    public List<Store> getAll() {
        return storeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }

    @Override
    public void saveToken(Store store, String token) {
        store.setToken(token);
        storeRepository.save(store);
    }

    @Override
    public Store getStoreByEmail(String email) {
        Optional<Store> storeOptional = storeRepository.findByEmail(email);

        if (storeOptional.isEmpty()) {
            return null;
        }

        return storeOptional.get();
    }
}
