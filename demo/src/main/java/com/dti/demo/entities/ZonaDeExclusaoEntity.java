package com.dti.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaDeExclusaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;
    private int raio;

    public boolean contem(int xDestino, int yDestino) {
        int dx = this.x - xDestino;
        int dy = this.y - yDestino;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        return distancia <= raio;
    }
}