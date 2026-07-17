package com.flow.payflow.config.security;

import com.flow.payflow.entity.Store;
import com.flow.payflow.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private StoreService storeService;

    @BeforeEach
    void setup() throws Exception {
        // set secret via reflection
        Field f = TokenService.class.getDeclaredField("secret");
        f.setAccessible(true);
        f.set(tokenService, "mysecret1234567890");
    }

    @Test
    void generateToken_shouldReturnNonEmptyToken_andSaveToken() {
        Store store = new Store();
        store.setEmail("user@test.com");

        // saveToken is void, so stub as doNothing
        doNothing().when(storeService).saveToken(any(Store.class), anyString());

        String token = tokenService.generateToken(store);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(storeService).saveToken(eq(store), anyString());
    }

    @Test
    void validateToken_returnsEmptyForInvalidToken() {
        String invalid = "invalid.token";
        String res = tokenService.validateToken(invalid);
        assertEquals("", res);
    }
}
