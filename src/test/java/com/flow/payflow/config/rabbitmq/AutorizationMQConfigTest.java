package com.flow.payflow.config.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class AutorizationMQConfigTest {

    private final AutorizationMQConfig config = new AutorizationMQConfig();

    @Test
    void queue_shouldCreateDurableQueueWithExpectedName() {
        Queue q = config.queue();
        assertNotNull(q);
        assertEquals(AutorizationMQConfig.QUEUE_PAYMENT, q.getName());
        assertTrue(q.isDurable());
    }

    @Test
    void exchange_shouldCreateDirectExchangeWithExpectedName() {
        DirectExchange ex = config.exchange();
        assertNotNull(ex);
        assertEquals(AutorizationMQConfig.EXCHANGE_PAYMENT, ex.getName());
    }

    @Test
    void binding_shouldBindQueueToExchangeWithRoutingKey() {
        Queue q = config.queue();
        DirectExchange ex = config.exchange();
        Binding b = config.binding(q, ex);

        assertNotNull(b);
        assertEquals(AutorizationMQConfig.QUEUE_PAYMENT, b.getDestination());
        assertEquals(AutorizationMQConfig.EXCHANGE_PAYMENT, b.getExchange());
        assertEquals(AutorizationMQConfig.ROUTING_KEY_PAYMENT, b.getRoutingKey());
    }
}
