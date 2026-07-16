package com.flow.payflow.service.impl;

import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.entity.Store;
import com.flow.payflow.exception.MessageException;
import com.flow.payflow.repository.FeesConfigRepository;
import com.flow.payflow.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeesConfigServiceImplTest {

    @Mock
    private FeesConfigRepository repository;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private FeesConfigServiceImpl service;

    @Test
    void saveThrowsWhenStoreTokenEmpty() {
        FeesConfig cfg = new FeesConfig();
        Store store = new Store();
        store.setToken("");
        when(storeService.getById(1L)).thenReturn(store);

        MessageException ex = assertThrows(MessageException.class, () -> service.save(cfg, 1L));
        // MessageException stores the code/message in Exception#getMessage()
        assertEquals("Token_Not_Found", ex.getMessage());
    }

    @Test
    void getByStoreByTokenReturnsNullWhenEmpty() {
        Store store = new Store();
        store.setId(0L);
        when(storeService.getStoreByToken("t")).thenReturn(store);
        when(repository.findByStoreId(0L)).thenReturn(Optional.empty());

        assertNull(service.getByStoreByToken("t"));
    }
}
