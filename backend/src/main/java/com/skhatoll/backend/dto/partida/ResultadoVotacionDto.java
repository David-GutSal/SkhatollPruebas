package com.skhatoll.backend.dto.partida;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultadoVotacionDto {
    private Integer idSesion;
    private String tipo;
    private String nombreEliminado;
    private boolean empate;
}
