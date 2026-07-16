package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Customer;
import com.flow.payflow.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void saveDelegatesToRepository() {
        Customer c = new Customer();
        when(repository.save(c)).thenReturn(c);

        Customer result = service.save(c);

        assertSame(c, result);
    }
}
