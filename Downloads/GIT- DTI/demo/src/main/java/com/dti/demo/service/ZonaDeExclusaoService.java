package com.dti.demo.service;

import com.dti.demo.dto.ZonaDeExclusaoDTO;
import com.dti.demo.entities.ZonaDeExclusaoEntity;
import com.dti.demo.repository.ZonaDeExclusaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZonaDeExclusaoService {

    private final ZonaDeExclusaoRepository repository;

    public ZonaDeExclusaoDTO adicionar(ZonaDeExclusaoDTO dto) {
        ZonaDeExclusaoEntity zona = ZonaDeExclusaoEntity.builder()
                .x(dto.getX())
                .y(dto.getY())
                .raio(dto.getRaio())
                .build();
        return toDTO(repository.save(zona));
    }

    public List<ZonaDeExclusaoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public boolean estaDentroDeZonaRestrita(int x, int y) {
        return repository.findAll().stream().anyMatch(z -> z.contem(x, y));
    }

    private ZonaDeExclusaoDTO toDTO(ZonaDeExclusaoEntity entity) {
        return ZonaDeExclusaoDTO.builder()
                .x(entity.getX())
                .y(entity.getY())
                .raio(entity.getRaio())
                .build();
    }
}
