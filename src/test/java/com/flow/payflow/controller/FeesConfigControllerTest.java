package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.FeesConfigDto;
import com.flow.payflow.entity.FeesConfig;
import com.flow.payflow.mapper.FeesConfigMapper;
import com.flow.payflow.service.FeesConfigService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class FeesConfigControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private FeesConfigService feesConfigService;

    @Mock
    private FeesConfigMapper feesConfigMapper;

    @InjectMocks
    private FeesConfigController feesConfigController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(feesConfigController).build();
    }

    @Test
    void save_returnsCreated() throws Exception {
        FeesConfigDto dto = new FeesConfigDto();
        dto.setFees(10);
        dto.setStoreId(1L);

        FeesConfig entity = new FeesConfig();
        entity.setFees(10);

        when(feesConfigMapper.toEntity(any(FeesConfigDto.class))).thenReturn(entity);
        when(feesConfigService.save(any(FeesConfig.class), any(Long.class))).thenReturn(entity);

        mvc.perform(post("/api/transaction/store/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fees").value(10));
    }

    @Test
    void getByStoreToken_callsService() throws Exception {
        FeesConfig cfg = new FeesConfig();
        cfg.setFees(5);
        when(feesConfigService.getByStoreByToken(any(String.class))).thenReturn(cfg);

        mvc.perform(get("/api/transaction/store/config")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fees").value(5));
    }
}
