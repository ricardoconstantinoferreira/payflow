package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionStatusDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Store;
import com.flow.payflow.service.ChangeStatusService;
import com.flow.payflow.service.StoreService;
import com.flow.payflow.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private TransactionService transactionService;

    @Mock
    private ChangeStatusService changeStatusService;

    @Mock
    private StoreService storeService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void create_sendsToQueue_accepts() throws Exception {
        TransactionDto dto = new TransactionDto();
        dto.setOrderId("1");

        mvc.perform(post("/api/transaction/payment")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("processing"));
    }

    @Test
    void updateStatus_callsServices() throws Exception {
        TransactionStatusDto dto = new TransactionStatusDto();
        dto.setStatus("PAID");

        Store store = new Store();
        store.setWebhook("http://callback");
        when(storeService.getStoreByToken(any(String.class))).thenReturn(store);

        mvc.perform(put("/api/transaction/payment/status/1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("processing"));
    }

    @Test
    void getById_returnsResponse() throws Exception {
        TransactionResponse resp = new TransactionResponse(1L, "o", 10f, "BRL", 1, "card", "t", 10f);
        when(transactionService.getById(1L)).thenReturn(resp);

        mvc.perform(get("/api/transaction/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
