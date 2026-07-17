package com.flow.payflow.config.security;

import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreDetailsServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreDetailsServiceImpl service;

    @Test
    void loadUserByUsername_returnsUserDetails_whenStoreExists() {
        Store store = new Store();
        store.setEmail("u@a.com");
        store.setPassword("pass");
        when(storeRepository.findByEmail("u@a.com")).thenReturn(Optional.of(store));

        UserDetails ud = service.loadUserByUsername("u@a.com");
        assertNotNull(ud);
        assertEquals("u@a.com", ud.getUsername());
    }

    @Test
    void loadUserByUsername_throwsWhenNotFound() {
        when(storeRepository.findByEmail("no@a.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("no@a.com"));
    }
}
