package com.dti.demo.controller;

import com.dti.demo.dto.ZonaDeExclusaoDTO;
import com.dti.demo.service.ZonaDeExclusaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zonas")
@RequiredArgsConstructor
public class ZonaDeExclusaoController {

    private final ZonaDeExclusaoService service;

    @PostMapping
    public ResponseEntity<ZonaDeExclusaoDTO> adicionar(@RequestBody ZonaDeExclusaoDTO dto) {
        return ResponseEntity.ok(service.adicionar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ZonaDeExclusaoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/verificar-entrega")
    public ResponseEntity<String> verificarEntrega(@RequestParam int x, @RequestParam int y) {
        boolean dentro = service.estaDentroDeZonaRestrita(x, y);
        if (dentro) {
            return ResponseEntity.ok("Entrega nao pode ser realizada nesta coordenada: zona de exclusao.");
        }
        return ResponseEntity.ok("Entrega permitida nesta coordenada.");
    }
}
