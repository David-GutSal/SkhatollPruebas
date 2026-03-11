package com.skhatoll.backend.dto.partida;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VotoDto {
    private String nombreVotante;
    private String nombreObjetivo;
}
