package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.StoreDto;
import com.flow.payflow.entity.Store;
import com.flow.payflow.mapper.StoreMapper;
import com.flow.payflow.service.StoreService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(storeController).build();
    }

    @Test
    void create_returnsCreatedStore() throws Exception {
        StoreDto dto = new StoreDto();
        dto.setDescription("Loja A");

        Store entity = new Store();
        entity.setDescription("Loja A");

        when(storeMapper.toEntity(any(StoreDto.class))).thenReturn(entity);
        when(storeService.create(any(Store.class))).thenReturn(entity);

        mvc.perform(post("/api/transaction/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Loja A"));
    }
}
