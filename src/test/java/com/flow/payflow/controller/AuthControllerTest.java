package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.config.security.TokenService;
import com.flow.payflow.dto.AuthDto;
import com.flow.payflow.entity.Store;
import com.flow.payflow.exception.MessageException;
import com.flow.payflow.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new com.flow.payflow.exception.handler.MessageExceptionHandler())
                .build();
    }

    @Test
    void login_returnsStoreWithToken_onSuccess() throws Exception {
        AuthDto dto = new AuthDto("user@test.com", "pass");

        Store store = new Store();
        store.setEmail("user@test.com");

        when(storeRepository.findByEmail(dto.email())).thenReturn(Optional.of(store));
        when(tokenService.generateToken(any(Store.class))).thenReturn("tok");

        mvc.perform(post("/api/transaction/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    void login_throwsOnBadCredentials() throws Exception {
        AuthDto dto = new AuthDto("user@test.com", "bad");

        // authenticationManager.authenticate will throw, controller catches and throws MessageException
        doThrow(new RuntimeException("bad credentials"))
                .when(authenticationManager).authenticate(any());

        mvc.perform(post("/api/transaction/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Not_Found"))
                .andExpect(jsonPath("$.message").value("Usuário ou senha inválidos"));
    }
}
