package com.skhatoll.backend.service.interfaces.partida;

import com.skhatoll.backend.dto.partida.AbrirVotacionRequest;
import com.skhatoll.backend.dto.partida.ResultadoVotacionDto;
import com.skhatoll.backend.dto.partida.VotarRequest;
import com.skhatoll.backend.entities.SesionVotacion;

public interface IPartidaService {
    void confirmarMuerte(String codigo, Integer idUsuario);

    void votar(String codigo, VotarRequest request);

    ResultadoVotacionDto cerrarVotacion(String codigo, Integer idSesion);

    SesionVotacion abrirVotacion(String codigo, AbrirVotacionRequest request);

    void cambiarFase(String codigo);

}
