package com.flow.payflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.payflow.dto.CaptureDto;
import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.config.rabbitmq.CaptureMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CaptureControllerTest {

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CaptureController captureController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(captureController).build();
    }

    @Test
    void capture_sendsToQueue_andReturnsAccepted() throws Exception {
        CaptureDto dto = new CaptureDto();
        dto.setAmount(10f);

        mvc.perform(post("/api/transaction/token123/capture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        verify(rabbitTemplate).convertAndSend(eq(CaptureMQConfig.EXCHANGE_CAPTURE), eq(CaptureMQConfig.ROUTING_KEY_CAPTURE), any(CaptureApiDto.class));
    }
}
