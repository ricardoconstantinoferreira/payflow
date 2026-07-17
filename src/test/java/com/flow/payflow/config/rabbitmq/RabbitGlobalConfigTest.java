package com.flow.payflow.config.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RabbitGlobalConfigTest {

    private final RabbitGlobalConfig config = new RabbitGlobalConfig();

    @Test
    void messageConverter_shouldReturnJacksonConverter() {
        MessageConverter mc = config.messageConverter();
        assertNotNull(mc);
        assertTrue(mc instanceof JacksonJsonMessageConverter);
    }

    @Test
    void rabbitTemplate_shouldConfigureTemplateWithConverter() {
        ConnectionFactory cf = mock(ConnectionFactory.class);
        MessageConverter mc = config.messageConverter();

        RabbitTemplate rt = config.rabbitTemplate(cf, mc);
        assertNotNull(rt);
        assertSame(mc, rt.getMessageConverter());
    }
}
