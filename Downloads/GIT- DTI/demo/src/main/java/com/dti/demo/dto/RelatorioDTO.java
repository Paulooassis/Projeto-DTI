package com.dti.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelatorioDTO {

    private long totalEntregas;
    private double tempoMedioEntrega;
    private String droneMaisEficiente;
    private String mapaEntregas;
}
