package com.flow.payflow.config.component;

import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureResponseDto;
import com.flow.payflow.entity.Status;
import com.flow.payflow.entity.Transaction;
import com.flow.payflow.service.CaptureService;
import com.flow.payflow.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptureConsumerTest {

    @Mock
    private CaptureService captureService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CaptureConsumer consumer;

    private CaptureApiDto dto;

    @BeforeEach
    void setup() {
        dto = new CaptureApiDto();
        dto.setToken("tok123");
        dto.setAmount(50f);
    }

    @Test
    void consumeCaptureMessage_whenResponseHasTransactionId_updatesTransactionToCaptured() {
        CaptureResponseDto resp = new CaptureResponseDto();
        resp.setTransactionId("tx-1");

        when(captureService.capture(any(CaptureApiDto.class))).thenReturn(resp);

        Transaction transaction = new Transaction();
        transaction.setId(10L);
        when(transactionService.getByCardToken(dto.getToken())).thenReturn(transaction);

        consumer.consumeCaptureMessage(dto);

        verify(transactionService).updateStatus(transaction.getId(), Status.CAPTURED);
    }

    @Test
    void consumeCaptureMessage_whenNoTransactionId_doesNotUpdate() {
        CaptureResponseDto resp = new CaptureResponseDto();
        resp.setTransactionId("");

        when(captureService.capture(any(CaptureApiDto.class))).thenReturn(resp);

        consumer.consumeCaptureMessage(dto);

        verify(transactionService, never()).updateStatus(anyLong(), any(Status.class));
    }
}
