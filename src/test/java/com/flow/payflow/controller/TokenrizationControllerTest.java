package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;
import com.flow.payflow.usecase.VelocityCheck;
import com.flow.payflow.service.TokenrizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TokenrizationControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private TokenrizationService tokenrizationService;

    @Mock
    private VelocityCheck velocityCheck;

    @InjectMocks
    private TokenrizationController tokenrizationController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(tokenrizationController).build();
    }

    @Test
    void validate_successWhenCode00AndWithinVelocity() throws Exception {
        TokenrizationDto dto = new TokenrizationDto();
        dto.setCard("1234");
        dto.setCvv("123");
        dto.setVenc("10");
        dto.setHolderName("teste teste");

        TokenrizationResponseDto resp = new TokenrizationResponseDto();
        resp.setCode("00");
        resp.setBrand("teste");
        resp.setCardToken("testetoken");
        resp.setMessage("teste");
        resp.setExpirationMonth("10");
        resp.setExpirationYear("10");
        resp.setLastFourDigits("1234");

        // Stub the service to return the response for any deserialized dto instance
        when(tokenrizationService.getTokenrization(any(TokenrizationDto.class))).thenReturn(resp);
        // Stub velocity check to return true for this scenario (use direct literals)
        when(velocityCheck.check("1234", "t")).thenReturn(true);

        mvc.perform(post("/v1/transaction/tokenrization")
                .header("Authorization", "Bearer t")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"))
                .andExpect(jsonPath("$.cardToken").value("testetoken"))
                .andExpect(jsonPath("$.lastFourDigits").value("1234"));

        // verify external service calls were mocked and invoked with expected parameters
        verify(tokenrizationService).getTokenrization(any(TokenrizationDto.class));
        verify(velocityCheck).check("1234", "t");
    }
}
