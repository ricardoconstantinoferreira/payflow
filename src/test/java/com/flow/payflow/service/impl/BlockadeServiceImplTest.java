package com.flow.payflow.service.impl;

import com.flow.payflow.entity.Blockade;
import com.flow.payflow.entity.Store;
import com.flow.payflow.entity.TypeParameter;
import com.flow.payflow.repository.BlockadeRepository;
import com.flow.payflow.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockadeServiceImplTest {

    @Mock
    private BlockadeRepository repository;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private BlockadeServiceImpl service;

    @Test
    void saveConvertsHoursToMinutesAndSaves() {
        Store store = new Store();
        store.setId(1L);
        when(storeService.getStoreByToken("t")).thenReturn(store);

        Blockade b = new Blockade();
        b.setTypeParameter(TypeParameter.HOURS);
        b.setParameter(2L);

        when(repository.findByStoreId(1L)).thenReturn(Optional.empty());
        when(repository.save(b)).thenReturn(b);

        Blockade result = service.save(b, "t");

        assertEquals(Long.valueOf(120L), result.getParameter());
    }
}
