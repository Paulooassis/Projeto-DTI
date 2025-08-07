package com.dti.demo.service;

import com.dti.demo.dto.DroneDTO;
import com.dti.demo.entities.DroneEntity;
import com.dti.demo.enums.DroneState;
import com.dti.demo.repository.DroneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    public List<DroneDTO> listarTodos() {
        return droneRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DroneEntity buscarPorId(int id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drone não encontrado"));
    }

    public DroneDTO criarDrone(DroneDTO dto) {
        DroneEntity drone = toEntity(dto);
        drone.setEstado(DroneState.IDLE);
        drone.setDisponivel(true);
        return toDTO(droneRepository.save(drone));
    }

    public DroneDTO atualizarDrone(int id, DroneDTO atualizado) {
        DroneEntity drone = droneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drone não encontrado"));

        drone.setPesoSuportado(atualizado.getPesoSuportado());
        drone.setDistanciaSuportada(atualizado.getDistanciaSuportada());
        drone.setBateria(atualizado.getBateria());

        return toDTO(droneRepository.save(drone));
    }

    public void deletarDrone(int id) {
        DroneEntity drone = droneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drone não encontrado"));
        droneRepository.delete(drone);
    }

    public void recarregarDrone(int id) {
        DroneEntity drone = droneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drone não encontrado"));

        drone.setBateria(100);
        drone.setDisponivel(true);
        drone.setEstado(DroneState.IDLE);
        droneRepository.save(drone);
    }

    public DroneEntity buscarEntidade(int id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drone não encontrado"));
    }

    public DroneEntity salvar(DroneEntity drone) {
        return droneRepository.save(drone);
    }

    private DroneDTO toDTO(DroneEntity drone) {
        return DroneDTO.builder()
                .id(drone.getId())
                .pesoSuportado(drone.getPesoSuportado())
                .distanciaSuportada(drone.getDistanciaSuportada())
                .bateria(drone.getBateria())
                .disponivel(drone.isDisponivel())
                .estado(drone.getEstado())
                .build();
    }

    private DroneEntity toEntity(DroneDTO dto) {
        return DroneEntity.builder()
                .id(dto.getId())
                .pesoSuportado(dto.getPesoSuportado())
                .distanciaSuportada(dto.getDistanciaSuportada())
                .bateria(dto.getBateria())
                .disponivel(dto.isDisponivel())
                .estado(dto.getEstado() != null ? dto.getEstado() : DroneState.IDLE)
                .build();
    }
}