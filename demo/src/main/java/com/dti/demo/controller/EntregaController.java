package com.dti.demo.controller;

import com.dti.demo.service.EntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping
    public ResponseEntity<String> realizarEntrega(@RequestParam int droneId, @RequestParam(defaultValue = "false") boolean simularFila
    ) {
        String resultado = entregaService.realizarEntrega(droneId, simularFila);
        return ResponseEntity.ok(resultado);
    }
}

