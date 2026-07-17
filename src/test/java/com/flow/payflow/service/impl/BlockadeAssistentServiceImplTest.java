package com.flow.payflow.service.impl;

import com.flow.payflow.entity.BlockadeAssistent;
import com.flow.payflow.repository.BlockadeAssistentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockadeAssistentServiceImplTest {

    @Mock
    private BlockadeAssistentRepository repository;

    @InjectMocks
    private BlockadeAssistentServiceImpl service;

    @Test
    void saveDelegates() {
        BlockadeAssistent b = new BlockadeAssistent();
        when(repository.save(b)).thenReturn(b);

        BlockadeAssistent result = service.save(b);

        assertSame(b, result);
    }

    @Test
    void getByCardReturnsNullWhenEmpty() {
        when(repository.findByCard("c")).thenReturn(Optional.empty());

        assertSame(null, service.getByCard("c"));
    }
}
