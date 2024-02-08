package com.mete;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private String mailingQueueName = "mailingQueue";
    private String notificationQueueName = "notificationQueue";
    private String routing_mailing = "routingMailing";
    private String routing_notification = "routingNotification";

    @Bean(name = "MAILING_QUEUE")
    public Queue mailingQueue(){
        return new Queue(this.mailingQueueName);
    }

    @Bean(name = "NOTIFICATION_QUEUE")
    public Queue notificationQueue(){
        return new Queue(this.notificationQueueName);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("MyExchange");
    }

    @Bean
    public Binding binding_mailing(){
        return BindingBuilder
                .bind(mailingQueue())
                .to(directExchange())
                .with(this.routing_mailing);
    }

    @Bean
    public Binding binding_notification(){
        return BindingBuilder
                .bind(notificationQueue())
                .to(directExchange())
                .with(this.routing_notification);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
