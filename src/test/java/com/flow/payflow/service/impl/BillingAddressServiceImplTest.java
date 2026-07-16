package com.flow.payflow.service.impl;

import com.flow.payflow.entity.BillingAddress;
import com.flow.payflow.repository.BillingAddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingAddressServiceImplTest {

    @Mock
    private BillingAddressRepository repository;

    @InjectMocks
    private BillingAddressServiceImpl service;

    @Test
    void saveDelegatesToRepository() {
        BillingAddress address = new BillingAddress();
        when(repository.save(address)).thenReturn(address);

        BillingAddress result = service.save(address);

        assertSame(address, result);
    }
}
