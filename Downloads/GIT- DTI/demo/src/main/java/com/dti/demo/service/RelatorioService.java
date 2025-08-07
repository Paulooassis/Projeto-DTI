package com.dti.demo.service;

import com.dti.demo.dto.RelatorioDTO;
import com.dti.demo.entities.*;
import com.dti.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final VooRepository vooRepository;
    private final PedidoRepository pedidoRepository;

    public RelatorioDTO gerarRelatorioCompleto() {
        long total = pedidoRepository.findAll()
                .stream().filter(PedidoEntity::isEntregue).count();

        double tempoMedio = vooRepository.findAll().stream()
                .mapToDouble(VooEntity::getTempoEstimado)
                .average().orElse(0.0);

        String droneEficiente = vooRepository.findAll().stream()
                .collect(Collectors.groupingBy(VooEntity::getDrone, Collectors.summingDouble(VooEntity::getDistanciaTotal)))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(e -> "Drone ID " + e.getKey().getId() + " (" + e.getValue() + " km)")
                .orElse("Nenhum voo registrado");

        String mapa = gerarMapaASCII();

        return RelatorioDTO.builder()
                .totalEntregas(total)
                .tempoMedioEntrega(Math.round(tempoMedio * 100.0) / 100.0)
                .droneMaisEficiente(droneEficiente)
                .mapaEntregas(mapa)
                .build();
    }

    private String gerarMapaASCII() {
        StringBuilder mapa = new StringBuilder();
        int tamanho = 10;

        List<PedidoEntity> entregues = pedidoRepository.findAll()
                .stream().filter(PedidoEntity::isEntregue).toList();

        for (int y = 0; y <= tamanho; y++) {
            for (int x = 0; x <= tamanho; x++) {
                boolean temEntrega = entregues.stream()
                        .anyMatch(p -> p.getCoordenada().getX() == x && p.getCoordenada().getY() == y);
                mapa.append(temEntrega ? "📦 " : "⬜ ");
            }
            mapa.append("\n");
        }

        return mapa.toString();
    }
}