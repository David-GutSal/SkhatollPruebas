package com.skhatoll.backend.dto.sala;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JugadorDto {
    private Integer idUsuario;
    private String nombre;
    private String codigoUuid;
    private Boolean estaVivo;
}