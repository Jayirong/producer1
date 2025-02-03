package com.anemona.producer1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.anemona.producer1.service.Producer1Service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("api/producer1")
public class Producer1Controller {

    @Autowired
    private Producer1Service producer;

    @PostMapping("/procesar-estado/{estadoVitalId}")
    public ResponseEntity<?> procesarEstadoVital(@PathVariable Long estadoVitalId) {
        try {
            producer.procesarEstadoVital(estadoVitalId);
            return ResponseEntity.ok("Estado vital procesado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error procesando estado vital: " + e.getMessage());
        }
    }
    
    
}
