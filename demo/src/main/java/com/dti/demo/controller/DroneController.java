package com.dti.demo.controller;

import com.dti.demo.dto.DroneDTO;
import com.dti.demo.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public ResponseEntity<List<DroneDTO>> listar() {
        return ResponseEntity.ok(droneService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneDTO> buscar(@PathVariable int id) {
        return ResponseEntity.ok(droneService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DroneDTO> criar(@RequestBody DroneDTO droneDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(droneService.criarDrone(droneDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DroneDTO> atualizar(@PathVariable int id, @RequestBody DroneDTO atualizado) {
        return ResponseEntity.ok(droneService.atualizarDrone(id, atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        droneService.deletarDrone(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/recarregar")
    public ResponseEntity<Void> recarregar(@PathVariable int id) {
        droneService.recarregarDrone(id);
        return ResponseEntity.ok().build();
    }
}
