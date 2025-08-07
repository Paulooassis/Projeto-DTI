package com.dti.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "voo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VooEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double distanciaTotal;

    private double tempoEstimado;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private DroneEntity drone;


    public void calcularTempoEstimado() {
        this.tempoEstimado = distanciaTotal/drone.VELOCIDADE;
    }
}

