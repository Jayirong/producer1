package com.anemona.producer1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.anemona.producer1.service.Producer1Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Producer1Controller {

    @Autowired
    private Producer1Service producer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        producer.sendMessage(message);
        return "Mensaje enviao: " + message;
    }
    
    
}
