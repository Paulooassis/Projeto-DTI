package com.dti.demo.service;

import com.dti.demo.entities.DroneEntity;
import com.dti.demo.entities.*;
import com.dti.demo.enums.DroneState;
import com.dti.demo.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


@Service
@RequiredArgsConstructor
public class EntregaService {

    private final PedidoRepository pedidoRepo;
    private final DroneService droneService;
    private final VooRepository vooRepo;

    public String realizarEntrega(int droneId, boolean simularFila) {
        DroneEntity drone = droneService.buscarPorId(droneId);

        if (!drone.isDisponivel()) {
            return "Drone indisponível ou com bateria insuficiente.";
        }

        drone.setEstado(DroneState.CARREGANDO);
        droneService.salvar(drone);

        List<PedidoEntity> pedidos = simularFila
                ? simularFilaDeEntrega()
                : pedidoRepo.findByEntregueFalseOrderByPrioridadeDescIdAsc();

        List<PedidoEntity> entregas = new ArrayList<>();
        double pesoTotal = 0;
        double distanciaTotal = 0;

        for (PedidoEntity pedido : pedidos) {
            double distancia = pedido.getCoordenada().distanciaPara(new Coordenada(0, 0)) * 2;

            if (pesoTotal + pedido.getPeso() <= drone.getPesoSuportado()
                    && distanciaTotal + distancia <= drone.getDistanciaSuportada()
                    && drone.getBateria() - distancia >= 10) {

                entregas.add(pedido);
                pesoTotal += pedido.getPeso();
                distanciaTotal += distancia;
            }
        }

        if (entregas.isEmpty()) {
            drone.setEstado(DroneState.IDLE);
            droneService.salvar(drone);
            return "Nenhum pedido pôde ser alocado para esse drone.";
        }

        drone.setEstado(DroneState.EM_VOO);
        droneService.salvar(drone);

        double tempoEntrega = calcularTempo(distanciaTotal);

        for (PedidoEntity pedido : entregas) {
            pedido.setEntregue(true);
        }

        VooEntity voo = VooEntity.builder()
                .drone(drone)
                .distanciaTotal(distanciaTotal)
                .tempoEstimado(tempoEntrega)
                .build();

        vooRepo.save(voo);

        for (PedidoEntity pedido : entregas) {
            pedido.setVoo(voo);
            pedidoRepo.save(pedido);
        }

        drone.reduzirBateria(distanciaTotal);
        drone.setEstado(drone.getBateria() <= 10 ? DroneState.RETORNANDO : DroneState.IDLE);
        drone.setDisponivel(drone.getBateria() > 10);
        droneService.salvar(drone);

        return "Entrega realizada com sucesso. " + entregas.size() + " pedidos entregues.";
    }

    private double calcularTempo(double distancia) {
        return (distancia / DroneEntity.VELOCIDADE) * 60.0;
    }

    private List<PedidoEntity> simularFilaDeEntrega() {
        PriorityQueue<PedidoEntity> fila = new PriorityQueue<>(
                Comparator.comparing(PedidoEntity::getPrioridade).reversed()
                        .thenComparing(PedidoEntity::getId)
        );
        fila.addAll(pedidoRepo.findByEntregueFalseOrderByPrioridadeDescIdAsc());
        return fila.stream().toList();
    }
}
