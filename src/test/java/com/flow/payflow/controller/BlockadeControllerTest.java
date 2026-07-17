package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.BlockadeDto;
import com.flow.payflow.entity.Blockade;
import com.flow.payflow.mapper.BlockadeMapper;
import com.flow.payflow.service.BlockadeService;
import com.flow.payflow.config.security.TokenService;
import com.flow.payflow.repository.StoreRepository;
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
class BlockadeControllerTest {

    private MockMvc mvc;

    // Use a locally constructed ObjectMapper to avoid requiring a Jackson bean in the test context
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private BlockadeService blockadeService;

    @Mock
    private BlockadeMapper blockadeMapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private BlockadeController blockadeController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(blockadeController).build();
    }

    @Test
    void save_returnsCreated() throws Exception {
        BlockadeDto dto = new BlockadeDto();
        dto.setParameter(1L);

        Blockade entity = new Blockade();
        entity.setParameter(1L);

        when(blockadeMapper.toEntity(any(BlockadeDto.class))).thenReturn(entity);
        when(blockadeService.save(any(Blockade.class), any(String.class))).thenReturn(entity);

        mvc.perform(post("/api/transaction/blockade")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parameter").value(1));
    }
}
