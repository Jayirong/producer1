package com.anemona.producer1.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Producer1Config {

    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", true);
    }
    
}