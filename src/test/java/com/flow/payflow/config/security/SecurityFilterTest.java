package com.flow.payflow.config.security;

import com.flow.payflow.entity.Store;
import com.flow.payflow.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void doFilterInternal_withNoAuthorization_callsFilterChainWithoutAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        securityFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withValidToken_setsAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(tokenService.validateToken("token123")).thenReturn("user@a.com");

        Store store = new Store();
        store.setEmail("user@a.com");
        store.setPassword("pwd");
        when(storeRepository.findByEmail("user@a.com")).thenReturn(Optional.of(store));

        securityFilter.doFilterInternal(request, response, filterChain);

        // should call filterChain and set security context
        verify(filterChain).doFilter(request, response);
        assertNotNull(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication());
    }
}
