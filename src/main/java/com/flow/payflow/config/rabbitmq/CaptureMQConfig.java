package com.flow.payflow.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptureMQConfig {

    public static final String QUEUE_CAPTURE = "capture.v1.execute-capture";
    public static final String EXCHANGE_CAPTURE = "capture.v1.exchange";
    public static final String ROUTING_KEY_CAPTURE = "capture.v1.routing-key";

    @Bean(name = "captureQueue")
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_CAPTURE).build();
    }

    @Bean(name = "exchangeCapture")
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_CAPTURE);
    }

    @Bean(name = "bindingCapture")
    public Binding binding(
            @Qualifier("captureQueue") Queue queue,
            @Qualifier("exchangeCapture") DirectExchange exchange
    ) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_CAPTURE);
    }

    @Bean(name = "converterCapture")
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
