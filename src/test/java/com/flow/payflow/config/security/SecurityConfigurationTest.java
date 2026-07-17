package com.flow.payflow.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigurationTest {

    private final SecurityConfiguration config = new SecurityConfiguration();

    @Test
    void passwordEncoder_shouldReturnBCrypt() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void authenticationManager_delegatesToAuthenticationConfiguration() throws Exception {
        AuthenticationConfiguration ac = mock(AuthenticationConfiguration.class);
        AuthenticationManager am = mock(AuthenticationManager.class);
        when(ac.getAuthenticationManager()).thenReturn(am);

        AuthenticationManager result = config.authenticationManager(ac);
        assertSame(am, result);
    }
}
