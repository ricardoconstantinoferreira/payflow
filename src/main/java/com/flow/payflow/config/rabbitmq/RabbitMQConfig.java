package com.flow.payflow.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PAYMENT = "payment.v1.execute-payment";
    public static final String EXCHANGE_PAYMENT = "payment.v1.exchange";
    public static final String ROUTING_KEY_PAYMENT = "payment.v1.routing-key";

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_PAYMENT).build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_PAYMENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
