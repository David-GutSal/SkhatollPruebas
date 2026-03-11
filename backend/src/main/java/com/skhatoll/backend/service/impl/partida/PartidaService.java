package com.skhatoll.backend.service.impl.partida;

import com.skhatoll.backend.dto.partida.*;
import com.skhatoll.backend.entities.SesionVotacion;
import com.skhatoll.backend.entities.Voto;
import com.skhatoll.backend.repository.SesionVotacionRepository;
import com.skhatoll.backend.repository.VotoRepository;
import com.skhatoll.backend.entities.Sala;
import com.skhatoll.backend.entities.SalaUsuario;
import com.skhatoll.backend.repository.SalaRepository;
import com.skhatoll.backend.repository.SalaUsuarioRepository;
import com.skhatoll.backend.entities.Usuario;
import com.skhatoll.backend.repository.UsuarioRepository;
import com.skhatoll.backend.service.interfaces.partida.IPartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaService  implements IPartidaService {

    private final SalaRepository salaRepository;
    private final SalaUsuarioRepository salaUsuarioRepository;
    private final SesionVotacionRepository sesionVotacionRepository;
    private final VotoRepository votoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PartidaSocketService partidaSocketService;

    // -------------------------------------------------------
    // Obtener el usuario autenticado desde el contexto de Security
    // -------------------------------------------------------
    private Usuario getUsuarioAutenticado() {
        String nombre = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    // -------------------------------------------------------
    // Obtener sala iniciada por código
    // -------------------------------------------------------
    private Sala getSalaIniciada(String codigoSala) {
        Sala sala = salaRepository.findByCodigoSala(codigoSala)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        if (sala.getEstadoSala() != Sala.EstadoSala.INICIADA) {
            throw new IllegalStateException("La partida no está en curso");
        }

        return sala;
    }

    // -------------------------------------------------------
    // Cambiar fase DÍA/NOCHE
    // -------------------------------------------------------
    @Transactional
    public void cambiarFase(String codigoSala) {
        Usuario solicitante = getUsuarioAutenticado();
        Sala sala = getSalaIniciada(codigoSala);

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el narrador puede cambiar la fase");
        }

        // Comprobar que no hay votaciones abiertas
        if (sesionVotacionRepository.existsBySala_IdSalaAndAbiertaTrue(sala.getIdSala())) {
            throw new IllegalStateException("Debes cerrar la votación antes de cambiar la fase");
        }

        // Alternar fase
        Sala.EstadoDia nuevaFase = sala.getEstadoDia() == Sala.EstadoDia.DIA
                ? Sala.EstadoDia.NOCHE
                : Sala.EstadoDia.DIA;

        // Si pasamos a DÍA incrementamos la ronda
        if (nuevaFase == Sala.EstadoDia.DIA) {
            sala.setRondaActual(sala.getRondaActual() + 1);
        }

        sala.setEstadoDia(nuevaFase);
        salaRepository.save(sala);

        partidaSocketService.notificarCambioFase(codigoSala, nuevaFase);
    }

    // -------------------------------------------------------
    // Abrir sesión de votación
    // -------------------------------------------------------
    @Transactional
    public SesionVotacion abrirVotacion(String codigoSala, AbrirVotacionRequest request) {
        Usuario solicitante = getUsuarioAutenticado();
        Sala sala = getSalaIniciada(codigoSala);

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el narrador puede abrir votaciones");
        }

        // No puede haber dos votaciones abiertas a la vez
        if (sesionVotacionRepository.existsBySala_IdSalaAndAbiertaTrue(sala.getIdSala())) {
            throw new IllegalStateException("Ya hay una votación abierta en esta sala");
        }

        SesionVotacion sesion = SesionVotacion.builder()
                .sala(sala)
                .tipo(request.getTipo())
                .ronda(sala.getRondaActual())
                .build();

        sesionVotacionRepository.save(sesion);

        partidaSocketService.notificarVotacion(
                codigoSala, sesion.getIdSesion(), sesion.getTipo().name(), true);

        return sesion;
    }

    // -------------------------------------------------------
    // Cerrar sesión de votación y calcular resultado
    // -------------------------------------------------------
    @Transactional
    public ResultadoVotacionDto cerrarVotacion(String codigoSala, Integer idSesion) {
        Usuario solicitante = getUsuarioAutenticado();
        Sala sala = getSalaIniciada(codigoSala);

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el narrador puede cerrar votaciones");
        }

        SesionVotacion sesion = sesionVotacionRepository.findById(idSesion)
                .orElseThrow(() -> new IllegalArgumentException("Sesión de votación no encontrada"));

        if (!sesion.getAbierta()) {
            throw new IllegalStateException("La sesión ya está cerrada");
        }

        // Cerrar la sesión
        sesion.setAbierta(false);
        sesion.setFechaCierre(LocalDateTime.now());
        sesionVotacionRepository.save(sesion);

        // Calcular resultado
        ResultadoVotacionDto resultado = calcularResultado(sesion);

        partidaSocketService.notificarVotacion(
                codigoSala, sesion.getIdSesion(), sesion.getTipo().name(), false);
        partidaSocketService.notificarResultadoVotacion(codigoSala, resultado);

        return resultado;
    }

    // -------------------------------------------------------
    // Emitir o cambiar voto (upsert)
    // -------------------------------------------------------
    @Transactional
    public void votar(String codigoSala, VotarRequest request) {
        Usuario votante = getUsuarioAutenticado();
        Sala sala = getSalaIniciada(codigoSala);

        // Comprobar que hay una sesión abierta
        SesionVotacion sesion = sesionVotacionRepository
                .findBySala_IdSalaAndAbiertaTrue(sala.getIdSala())
                .orElseThrow(() -> new IllegalStateException("No hay ninguna votación abierta"));

        // Comprobar que el votante está vivo en la sala
        SalaUsuario salaUsuario = salaUsuarioRepository
                .findBySala_IdSalaAndUsuario_IdUsuario(sala.getIdSala(), votante.getIdUsuario())
                .orElseThrow(() -> new IllegalStateException("No estás en esta sala"));

        if (!salaUsuario.getEstaVivo()) {
            throw new IllegalStateException("Los jugadores eliminados no pueden votar");
        }

        // Comprobar que el objetivo existe y está vivo
        Usuario objetivo = usuarioRepository.findById(request.getIdObjetivo())
                .orElseThrow(() -> new IllegalArgumentException("Jugador objetivo no encontrado"));

        SalaUsuario objetivoEnSala = salaUsuarioRepository
                .findBySala_IdSalaAndUsuario_IdUsuario(sala.getIdSala(), objetivo.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El objetivo no está en esta sala"));

        if (!objetivoEnSala.getEstaVivo()) {
            throw new IllegalStateException("No puedes votar a un jugador eliminado");
        }

        // Upsert: actualizar voto existente o crear uno nuevo
        Optional<Voto> votoExistente = votoRepository
                .findBySesion_IdSesionAndVotante_IdUsuario(
                        sesion.getIdSesion(), votante.getIdUsuario());

        Voto voto;
        if (votoExistente.isPresent()) {
            voto = votoExistente.get();
            voto.setObjetivo(objetivo);
            voto.setFechaVoto(LocalDateTime.now());
        } else {
            voto = Voto.builder()
                    .sesion(sesion)
                    .votante(votante)
                    .objetivo(objetivo)
                    .build();
        }

        votoRepository.save(voto);

        // Notificar lista de votos actualizada en tiempo real
        List<VotoDto> votos = votoRepository.findBySesion_IdSesion(sesion.getIdSesion())
                .stream()
                .map(v -> new VotoDto(
                        v.getVotante().getNombre(),
                        v.getObjetivo().getNombre()))
                .toList();

        partidaSocketService.notificarVotos(codigoSala, votos);
    }

    // -------------------------------------------------------
    // Confirmar muerte de un jugador
    // -------------------------------------------------------
    @Transactional
    public void confirmarMuerte(String codigoSala, Integer idUsuario) {
        Usuario solicitante = getUsuarioAutenticado();
        Sala sala = getSalaIniciada(codigoSala);

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el narrador puede confirmar muertes");
        }

        SalaUsuario salaUsuario = salaUsuarioRepository
                .findBySala_IdSalaAndUsuario_IdUsuario(sala.getIdSala(), idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El jugador no está en esta sala"));

        if (!salaUsuario.getEstaVivo()) {
            throw new IllegalStateException("El jugador ya está eliminado");
        }

        // Confirmar muerte y revelar rol
        salaUsuario.setEstaVivo(false);
        salaUsuario.setMuerteConfirmada(true);
        salaUsuarioRepository.save(salaUsuario);

        MuerteConfirmadaDto muerte = new MuerteConfirmadaDto(
                salaUsuario.getUsuario().getNombre(),
                salaUsuario.getRol().getNombre(),
                salaUsuario.getRol().getBando().name());

        partidaSocketService.notificarMuerte(codigoSala, muerte);

        // Comprobar condición de victoria tras cada muerte
        comprobarFinPartida(codigoSala, sala);
    }

    // -------------------------------------------------------
    // Comprobar si la partida ha terminado
    // -------------------------------------------------------
    private void comprobarFinPartida(String codigoSala, Sala sala) {
        List<SalaUsuario> jugadoresVivos = salaUsuarioRepository
                .findBySala_IdSala(sala.getIdSala())
                .stream()
                .filter(su -> su.getEstaVivo()
                        && !su.getUsuario().getIdUsuario()
                        .equals(sala.getNarrador().getIdUsuario()))
                .toList();

        long lobosVivos = jugadoresVivos.stream()
                .filter(su -> su.getRol() != null
                        && su.getRol().getBando().name().equals("lobo"))
                .count();

        long aldeaViva = jugadoresVivos.stream()
                .filter(su -> su.getRol() != null
                        && su.getRol().getBando().name().equals("aldea"))
                .count();

        String bandoGanador = null;
        String mensaje = null;

        // Aldeanos ganan si no quedan lobos
        if (lobosVivos == 0) {
            bandoGanador = "aldea";
            mensaje = "¡La aldea ha eliminado a todos los hombres lobo!";
        }
        // Lobos ganan si igualan o superan a los aldeanos
        else if (lobosVivos >= aldeaViva) {
            bandoGanador = "lobo";
            mensaje = "¡Los hombres lobo dominan la aldea!";
        }

        if (bandoGanador != null) {
            sala.setEstadoSala(Sala.EstadoSala.CERRADA);
            salaRepository.save(sala);
            partidaSocketService.notificarFinPartida(
                    codigoSala, new FinPartidaDto(bandoGanador, mensaje));
        }
    }

    // -------------------------------------------------------
    // Calcular resultado de una votación cerrada
    // -------------------------------------------------------
    private ResultadoVotacionDto calcularResultado(SesionVotacion sesion) {
        List<Voto> votos = votoRepository.findBySesion_IdSesion(sesion.getIdSesion());

        if (votos.isEmpty()) {
            return new ResultadoVotacionDto(
                    sesion.getIdSesion(), sesion.getTipo().name(), null, true);
        }

        // Contar votos por objetivo
        Map<String, Long> conteo = votos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getObjetivo().getNombre(),
                        Collectors.counting()));

        long maxVotos = conteo.values().stream().mapToLong(Long::longValue).max().orElse(0);

        List<String> masVotados = conteo.entrySet().stream()
                .filter(e -> e.getValue() == maxVotos)
                .map(Map.Entry::getKey)
                .toList();

        // Empate si hay más de un jugador con el máximo de votos
        if (masVotados.size() > 1) {
            return new ResultadoVotacionDto(
                    sesion.getIdSesion(), sesion.getTipo().name(), null, true);
        }

        return new ResultadoVotacionDto(
                sesion.getIdSesion(), sesion.getTipo().name(), masVotados.get(0), false);
    }
}