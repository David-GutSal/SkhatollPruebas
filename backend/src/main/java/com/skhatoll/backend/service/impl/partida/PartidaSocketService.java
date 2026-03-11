package com.skhatoll.backend.service.impl.partida;

import com.skhatoll.backend.dto.partida.FinPartidaDto;
import com.skhatoll.backend.dto.partida.MuerteConfirmadaDto;
import com.skhatoll.backend.dto.partida.ResultadoVotacionDto;
import com.skhatoll.backend.dto.partida.VotoDto;
import com.skhatoll.backend.entities.Sala;
import com.skhatoll.backend.service.interfaces.partida.IPartidaSocketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaSocketService implements IPartidaSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // -------------------------------------------------------
    // Notifica el cambio de fase DÍA/NOCHE
    // Canal: /topic/partida/{codigo}/fase
    // -------------------------------------------------------
    public void notificarCambioFase(String codigoSala, Sala.EstadoDia nuevaFase) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/fase",
                new FaseEvent("CAMBIO_FASE", nuevaFase.name()));
    }

    // -------------------------------------------------------
    // Notifica apertura o cierre de votación
    // Canal: /topic/partida/{codigo}/votacion
    // -------------------------------------------------------
    public void notificarVotacion(String codigoSala, Integer idSesion,
                                  String tipo, boolean abierta) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/votacion",
                new VotacionEvent(
                        abierta ? "VOTACION_ABIERTA" : "VOTACION_CERRADA",
                        idSesion,
                        tipo,
                        abierta));
    }

    // -------------------------------------------------------
    // Notifica el resultado al cerrar la votación
    // Canal: /topic/partida/{codigo}/votacion
    // -------------------------------------------------------
    public void notificarResultadoVotacion(String codigoSala, ResultadoVotacionDto resultado) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/votacion",
                resultado);
    }

    // -------------------------------------------------------
    // Notifica el estado actual de los votos en tiempo real
    // Canal: /topic/partida/{codigo}/votos
    // -------------------------------------------------------
    public void notificarVotos(String codigoSala, List<VotoDto> votos) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/votos",
                new VotosEvent("VOTOS_ACTUALIZADOS", votos));
    }

    // -------------------------------------------------------
    // Notifica una muerte confirmada por el narrador
    // Canal: /topic/partida/{codigo}/muerte
    // -------------------------------------------------------
    public void notificarMuerte(String codigoSala, MuerteConfirmadaDto muerte) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/muerte",
                muerte);
    }

    // -------------------------------------------------------
    // Notifica el fin de partida con el bando ganador
    // Canal: /topic/partida/{codigo}/fin
    // -------------------------------------------------------
    public void notificarFinPartida(String codigoSala, FinPartidaDto fin) {
        messagingTemplate.convertAndSend(
                "/topic/partida/" + codigoSala + "/fin",
                fin);
    }

    // -------------------------------------------------------
    // Eventos internos
    // -------------------------------------------------------

    @Data
    @AllArgsConstructor
    public static class FaseEvent {
        private String tipo;
        private String fase;
    }

    @Data
    @AllArgsConstructor
    public static class VotacionEvent {
        private String tipo;
        private Integer idSesion;
        private String tipoVotacion;
        private boolean abierta;
    }

    @Data
    @AllArgsConstructor
    public static class VotosEvent {
        private String tipo;
        private List<VotoDto> votos;
    }
}