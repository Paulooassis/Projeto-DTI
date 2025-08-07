package com.dti.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenada {
    private int x;
    private int y;

    public double distanciaPara(Coordenada outra) {
        int dx = this.x - outra.getX();
        int dy = this.y - outra.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

