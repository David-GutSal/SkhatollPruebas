package com.skhatoll.backend.service.impl.sala;

import com.skhatoll.backend.service.interfaces.sala.ISalaSocketService;
import com.skhatoll.backend.dto.sala.JugadorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaSocketService implements ISalaSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // -------------------------------------------------------
    // Notifica a todos en el lobby que un nuevo jugador se unió
    // Canal: /topic/sala/{codigo}
    // -------------------------------------------------------
    public void notificarNuevoJugador(String codigoSala, List<JugadorDto> jugadores) {
        messagingTemplate.convertAndSend(
                "/topic/sala/" + codigoSala,
                new JugadoresActualizadosEvent("JUGADOR_UNIDO", jugadores));
    }

    // -------------------------------------------------------
    // Notifica a todos que la partida ha comenzado
    // Canal: /topic/sala/{codigo}/inicio
    // -------------------------------------------------------
    public void notificarInicio(String codigoSala) {
        messagingTemplate.convertAndSend(
                "/topic/sala/" + codigoSala + "/inicio",
                new PartidaIniciadaEvent("PARTIDA_INICIADA"));
    }

    // -------------------------------------------------------
    // Envía el rol asignado de forma privada a cada jugador
    // Canal: /user/queue/rol  (solo llega al jugador concreto)
    // -------------------------------------------------------
    public void enviarRolPrivado(String nombreUsuario, RolAsignadoEvent evento) {
        messagingTemplate.convertAndSendToUser(
                nombreUsuario,
                "/queue/rol",
                evento);
    }

    // -------------------------------------------------------
    // Eventos (clases internas para serializar como JSON)
    // -------------------------------------------------------

    @Data
    @AllArgsConstructor
    public static class JugadoresActualizadosEvent {
        private String tipo;
        private List<JugadorDto> jugadores;
    }

    @Data
    @AllArgsConstructor
    public static class PartidaIniciadaEvent {
        private String tipo;
    }

    @Data
    @AllArgsConstructor
    public static class RolAsignadoEvent {
        private String tipo;
        private Integer idRol;
        private String nombreRol;
        private String descripcionRol;
        private String bando;
    }
}
