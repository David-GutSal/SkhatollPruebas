package com.skhatoll.backend.service.interfaces.sala;

import com.skhatoll.backend.dto.sala.AsignarNarradorRequest;
import com.skhatoll.backend.dto.sala.CrearSalaResponse;
import com.skhatoll.backend.dto.sala.JugadorDto;
import com.skhatoll.backend.dto.sala.UnirseRequest;

import java.util.List;

public interface ISalaService {
    CrearSalaResponse crearSala();

    void unirse(UnirseRequest request);

    List<JugadorDto> getJugadores(String codigo);

    void asignarNarrador(String codigo, AsignarNarradorRequest request);

    void iniciarPartida(String codigo);
}
