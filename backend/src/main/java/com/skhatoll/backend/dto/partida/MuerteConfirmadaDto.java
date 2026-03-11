package com.skhatoll.backend.dto.partida;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MuerteConfirmadaDto {
    private String nombreJugador;
    private String nombreRol;
    private String bando;
}
