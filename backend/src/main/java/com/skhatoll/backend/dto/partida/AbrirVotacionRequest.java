package com.skhatoll.backend.dto.partida;

import com.skhatoll.backend.entities.SesionVotacion;
import lombok.Data;

@Data
public class AbrirVotacionRequest {
    private SesionVotacion.TipoVotacion tipo;
}
