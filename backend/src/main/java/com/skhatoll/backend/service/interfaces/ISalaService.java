package com.skhatoll.backend.service.interfaces;

import com.skhatoll.backend.dto.AsignarNarradorRequest;
import com.skhatoll.backend.dto.CrearSalaResponse;
import com.skhatoll.backend.dto.JugadorDto;
import com.skhatoll.backend.dto.UnirseRequest;

import java.util.List;

public interface ISalaService {
    CrearSalaResponse crearSala();

    void unirse(UnirseRequest request);

    List<JugadorDto> getJugadores(String codigo);

    void asignarNarrador(String codigo, AsignarNarradorRequest request);

    void iniciarPartida(String codigo);
}
