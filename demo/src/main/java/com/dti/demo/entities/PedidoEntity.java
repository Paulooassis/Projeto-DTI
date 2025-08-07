package com.dti.demo.entities;

import com.dti.demo.enums.Prioridade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private Coordenada coordenada;

    private double peso;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private boolean entregue;

    @ManyToOne
    @JoinColumn(name = "voo_id")
    private VooEntity voo;
}
