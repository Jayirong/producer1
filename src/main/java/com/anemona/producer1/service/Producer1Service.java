package com.anemona.producer1.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anemona.producer1.DTO.EstadoVitalDTO;
import com.anemona.producer1.DTO.ParametrosVitalesDTO;
import com.anemona.producer1.config.Producer1Config;
import com.anemona.producer1.model.AlertaMensaje;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Producer1Service {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aneback.url}")
    private String anebackUrl;

    public void procesarEstadoVital(Long estadoVitalId){
        try {
            //obtenemos el estado vital desde aneback
            EstadoVitalDTO estadoVital = restTemplate.getForObject(
                anebackUrl + "/api/estadoVitales/" + estadoVitalId,
                EstadoVitalDTO.class
            );

            //obtenemos parametros vitales activos
            ParametrosVitalesDTO parametros = restTemplate.getForObject(
                anebackUrl + "/api/parametros-vitales/activos",
                ParametrosVitalesDTO.class
            );

            validarParametrosVitales(estadoVital, parametros);
        } catch (Exception e) {
            log.error("Error procesando estado vital: {}", e.getMessage());
        }
    }

    private void validarParametrosVitales(EstadoVitalDTO estadoVital, ParametrosVitalesDTO parametros) {
        validarFrecuenciaCardiaca(estadoVital, parametros);
        validarPresionArterial(estadoVital, parametros);
        validarSaturacionOxigeno(estadoVital, parametros);
    }

    private void validarFrecuenciaCardiaca(EstadoVitalDTO estadoVital, ParametrosVitalesDTO parametros) {
        if (estadoVital.getFrecuencia_cardiaca() < parametros.getFrecuencia_cardiaca_min() ||
            estadoVital.getFrecuencia_cardiaca() > parametros.getFrecuencia_cardiaca_max()) {
            
            AlertaMensaje alerta = new AlertaMensaje();
            alerta.setId_paciente(estadoVital.getId_paciente());
            alerta.setId_estado_vital(estadoVital.getId_estado());
            alerta.setParametro_alterado("frecuencia_cardiaca");
            alerta.setDescripcion_alerta("Frecuencia cardíaca fuera del rango normal");
            alerta.setNivel_alerta(2);

            enviarAlerta(alerta);
        }
    }

    private void validarPresionArterial(EstadoVitalDTO estadoVital, ParametrosVitalesDTO parametros) {
        //validamos la presion sistolica
        if (estadoVital.getPresion_arterial_sis() < parametros.getPresion_arterial_sis_min() ||
            estadoVital.getPresion_arterial_sis() > parametros.getPresion_arterial_sis_max()) {
            
            AlertaMensaje alerta = new AlertaMensaje();
            alerta.setId_paciente(estadoVital.getId_paciente());
            alerta.setId_estado_vital(estadoVital.getId_estado());
            alerta.setParametro_alterado("presion_arterial_sistolica");
            alerta.setDescripcion_alerta("Presión arterial sistólica fuera del rango normal");
            alerta.setNivel_alerta(2);

            enviarAlerta(alerta);
        }

        //validamos la presion diastólica
        if (estadoVital.getPresion_arterial_dias() < parametros.getPresion_arterial_dias_min() ||
            estadoVital.getPresion_arterial_dias() > parametros.getPresion_arterial_dias_max()) {
            
            AlertaMensaje alerta = new AlertaMensaje();
            alerta.setId_paciente(estadoVital.getId_paciente());
            alerta.setId_estado_vital(estadoVital.getId_estado());
            alerta.setParametro_alterado("presion_arterial_diastolica");
            alerta.setDescripcion_alerta("Presión arterial diastólica fuera del rango normal");
            alerta.setNivel_alerta(2);

            enviarAlerta(alerta);
        }
    }

    private void validarSaturacionOxigeno(EstadoVitalDTO estadoVital, ParametrosVitalesDTO parametros) {
        if (estadoVital.getSaturacion_oxigeno() < parametros.getSaturacion_oxigeno_min()) {
            AlertaMensaje alerta = new AlertaMensaje();
            alerta.setId_paciente(estadoVital.getId_paciente());
            alerta.setId_estado_vital(estadoVital.getId_estado());
            alerta.setParametro_alterado("saturacion_oxigeno");
            alerta.setDescripcion_alerta("Saturacion de oxigeno por debajo del nivel minimo");
            alerta.setNivel_alerta(3);

            enviarAlerta(alerta);
        }
    }

    private void enviarAlerta(AlertaMensaje alerta) {
        try {
            rabbitTemplate.convertAndSend(
                Producer1Config.EXCHANGE_ALERTAS,
                Producer1Config.ROUTING_KEY,
                alerta
            );
            log.info("Alerta enviada: {}", alerta);
        } catch (Exception e) {
            log.error("Error al enviar alerta: {}", e.getMessage());
        }
    }
        

}
