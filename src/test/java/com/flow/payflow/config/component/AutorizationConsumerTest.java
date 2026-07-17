package com.flow.payflow.config.component;

import com.flow.payflow.dto.AutorizationResponseDto;
import com.flow.payflow.dto.TransactionDto;
import com.flow.payflow.dto.TransactionResponse;
import com.flow.payflow.entity.Status;
import com.flow.payflow.service.AutorizationService;
import com.flow.payflow.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorizationConsumerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AutorizationService autorizationService;

    @InjectMocks
    private AutorizationConsumer consumer;

    private TransactionDto dto;

    @BeforeEach
    void setup() {
        dto = new TransactionDto();
        dto.setCardToken("tok123");
        dto.setAmount(10f);
    }

    @Test
    void consumeAutorizationMessage_whenAuthorizationSucceeds_updatesToAuthorized() {
        TransactionResponse response = new TransactionResponse(1L, "order", 10f, "BRL", 1, "card", "tok123", 10f);
        when(transactionService.create(dto)).thenReturn(response);

        AutorizationResponseDto autorizationResponse = new AutorizationResponseDto();
        autorizationResponse.setCode("00");
        when(autorizationService.autorization(dto, response.amountTotal())).thenReturn(autorizationResponse);

        consumer.consumeAutorizationMessage(dto);

        // verify updateStatus called with AUTHORIZED
        verify(transactionService).updateStatus(response.id(), Status.AUTHORIZED);
    }

    @Test
    void consumeAutorizationMessage_whenAuthorizationFails_updatesToFailed() {
        TransactionResponse response = new TransactionResponse(2L, "order2", 20f, "BRL", 1, "card", "tok123", 20f);
        when(transactionService.create(dto)).thenReturn(response);

        AutorizationResponseDto autorizationResponse = new AutorizationResponseDto();
        autorizationResponse.setCode("05");
        when(autorizationService.autorization(dto, response.amountTotal())).thenReturn(autorizationResponse);

        consumer.consumeAutorizationMessage(dto);

        // verify updateStatus called with FAILED
        verify(transactionService).updateStatus(response.id(), Status.FAILED);
    }
}
