package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Fees;
import com.flow.payflow.repository.FeesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeesServiceImplTest {

    @Mock
    private FeesRepository repository;

    @InjectMocks
    private FeesServiceImpl service;

    @Test
    void saveDelegatesToRepository() {
        Fees fees = new Fees();
        when(repository.save(fees)).thenReturn(fees);

        Fees result = service.save(fees);

        assertSame(fees, result);
    }
}
