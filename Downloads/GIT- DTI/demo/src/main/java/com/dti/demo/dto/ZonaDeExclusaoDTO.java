package com.dti.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaDeExclusaoDTO {
    private int x;
    private int y;
    private int raio;
}