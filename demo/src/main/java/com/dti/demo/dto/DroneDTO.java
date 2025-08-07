package com.dti.demo.dto;

import com.dti.demo.enums.DroneState;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

    private int id;
    private double pesoSuportado;
    private double distanciaSuportada;
    private int bateria;
    private boolean disponivel;
    private DroneState estado;
}

