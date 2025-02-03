package com.anemona.producer1.DTO;

import lombok.Data;

@Data
public class EstadoVitalDTO {
    private Long id_estado;
    private int frecuencia_cardiaca;
    private int presion_arterial_sis;
    private int presion_arterial_dias;
    private float saturacion_oxigeno;
    private Long id_paciente;
}
