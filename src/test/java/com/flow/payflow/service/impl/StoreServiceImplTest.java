package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @Mock
    private StoreRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StoreServiceImpl service;

    @Test
    void createEncodesPasswordAndSaves() {
        Store s = new Store();
        s.setPassword("plain");
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(repository.save(s)).thenReturn(s);

        Store result = service.create(s);

        assertEquals("encoded", result.getPassword());
    }
}
