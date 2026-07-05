package com.flow.payflow.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class AutorizationMQConfig {

    public static final String QUEUE_PAYMENT = "payment.v1.execute-payment";
    public static final String EXCHANGE_PAYMENT = "payment.v1.exchange";
    public static final String ROUTING_KEY_PAYMENT = "payment.v1.routing-key";

    @Bean(name = "autorizationQueue")
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_PAYMENT).build();
    }

    @Bean(name = "exchangeAutorization")
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }

    @Bean(name = "bindingAutorization")
    public Binding binding(
            @Qualifier("autorizationQueue") Queue queue,
            @Qualifier("exchangeAutorization") DirectExchange exchange
    ) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_PAYMENT);
    }

    @Bean(name = "converterAutorization")
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
