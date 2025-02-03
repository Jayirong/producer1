package com.anemona.producer1.model;

import lombok.Data;

@Data
public class AlertaMensaje {
    private Long id_paciente;
    private Long id_estado_vital;
    private String parametro_alterado;
    private String descripcion_alerta;
    private int nivel_alerta;
}
