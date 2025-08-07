package com.dti.demo.entities;

import com.dti.demo.enums.DroneState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "drone")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneEntity {

    public static final double VELOCIDADE = 40;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double pesoSuportado;

    private double distanciaSuportada;
    private boolean disponivel;
    private int bateria;

    @Enumerated(EnumType.STRING)
    private DroneState estado;

    public boolean isDisponivel() {
        if(bateria <= 10){
            return false;
        }
        return disponivel;
    }

    public void reduzirBateria(double distanciaPercorrida) {
        int reducao = (int) Math.ceil(distanciaPercorrida);
        int novaBateria = this.getBateria() - reducao;

        this.setBateria(Math.max(novaBateria, 0));
        if (this.getBateria() <= 10) {
            this.setDisponivel(false);
            this.setEstado(DroneState.RETORNANDO);
        }
    }
}
