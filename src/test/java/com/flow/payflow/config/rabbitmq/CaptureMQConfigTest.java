package com.flow.payflow.config.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class CaptureMQConfigTest {

    private final CaptureMQConfig config = new CaptureMQConfig();

    @Test
    void queue_shouldCreateDurableQueueWithExpectedName() {
        Queue q = config.queue();
        assertNotNull(q);
        assertEquals(CaptureMQConfig.QUEUE_CAPTURE, q.getName());
        assertTrue(q.isDurable());
    }

    @Test
    void exchange_shouldCreateDirectExchangeWithExpectedName() {
        DirectExchange ex = config.exchange();
        assertNotNull(ex);
        assertEquals(CaptureMQConfig.EXCHANGE_CAPTURE, ex.getName());
    }

    @Test
    void binding_shouldBindQueueToExchangeWithRoutingKey() {
        Queue q = config.queue();
        DirectExchange ex = config.exchange();
        Binding b = config.binding(q, ex);

        assertNotNull(b);
        assertEquals(CaptureMQConfig.QUEUE_CAPTURE, b.getDestination());
        assertEquals(CaptureMQConfig.EXCHANGE_CAPTURE, b.getExchange());
        assertEquals(CaptureMQConfig.ROUTING_KEY_CAPTURE, b.getRoutingKey());
    }
}
