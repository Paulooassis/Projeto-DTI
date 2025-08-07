package com.dti.demo.controller;

import com.dti.demo.dto.RelatorioDTO;
import com.dti.demo.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping
    public ResponseEntity<RelatorioDTO> gerarRelatorio() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioCompleto());
    }
}
