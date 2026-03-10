package com.skhatoll.backend.service.impl;

import com.skhatoll.backend.dto.*;
import com.skhatoll.backend.entities.Sala;
import com.skhatoll.backend.entities.SalaUsuario;
import com.skhatoll.backend.repository.SalaRepository;
import com.skhatoll.backend.repository.SalaUsuarioRepository;
import com.skhatoll.backend.entities.Usuario;
import com.skhatoll.backend.repository.UsuarioRepository;
import com.skhatoll.backend.service.interfaces.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SalaService implements ISalaService {

    private final SalaRepository salaRepository;
    private final SalaUsuarioRepository salaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        String nombre = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Transactional
    public CrearSalaResponse crearSala() {
        Usuario creador = getUsuarioAutenticado();

        String codigo = generarCodigoUnico();

        Sala sala = Sala.builder()
                .narrador(creador)
                .codigoSala(codigo)
                .build();

        salaRepository.save(sala);

        SalaUsuario salaUsuario = SalaUsuario.builder()
                .sala(sala)
                .usuario(creador)
                .build();

        salaUsuarioRepository.save(salaUsuario);

        return new CrearSalaResponse(codigo);
    }

    @Transactional
    public void unirse(UnirseRequest request) {
        Usuario usuario = getUsuarioAutenticado();

        Sala sala = salaRepository.findByCodigoSala(request.getCodigoSala())
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        if (sala.getEstadoSala() != Sala.EstadoSala.CREADA) {
            throw new IllegalStateException("La sala no está disponible para unirse");
        }

        int jugadoresActuales = salaUsuarioRepository.countBySala_IdSala(sala.getIdSala());
        if (jugadoresActuales >= sala.getMaxJugadores()) {
            throw new IllegalStateException("La sala está llena");
        }

        boolean yaEsta = salaUsuarioRepository.existsBySala_IdSalaAndUsuario_IdUsuario(
                sala.getIdSala(), usuario.getIdUsuario());
        if (yaEsta) {
            throw new IllegalStateException("Ya estás en esta sala");
        }

        SalaUsuario salaUsuario = SalaUsuario.builder()
                .sala(sala)
                .usuario(usuario)
                .build();

        salaUsuarioRepository.save(salaUsuario);
    }

    public List<JugadorDto> getJugadores(String codigoSala) {
        Sala sala = salaRepository.findByCodigoSala(codigoSala)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        return salaUsuarioRepository.findBySala_IdSala(sala.getIdSala())
                .stream()
                .map(su -> new JugadorDto(
                        su.getUsuario().getIdUsuario(),
                        su.getUsuario().getNombre(),
                        su.getUsuario().getCodigoUuid(),
                        su.getEstaVivo()))
                .toList();
    }

    @Transactional
    public void asignarNarrador(String codigoSala, AsignarNarradorRequest request) {
        Usuario solicitante = getUsuarioAutenticado();

        Sala sala = salaRepository.findByCodigoSala(codigoSala)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el creador puede asignar el narrador");
        }

        boolean estaEnSala = salaUsuarioRepository.existsBySala_IdSalaAndUsuario_IdUsuario(
                sala.getIdSala(), request.getIdUsuario());
        if (!estaEnSala) {
            throw new IllegalArgumentException("El usuario no está en la sala");
        }

        Usuario nuevoNarrador = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        sala.setNarrador(nuevoNarrador);
        salaRepository.save(sala);
    }

    @Transactional
    public void iniciarPartida(String codigoSala) {
        Usuario solicitante = getUsuarioAutenticado();

        Sala sala = salaRepository.findByCodigoSala(codigoSala)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        if (!sala.getNarrador().getIdUsuario().equals(solicitante.getIdUsuario())) {
            throw new IllegalStateException("Solo el narrador puede iniciar la partida");
        }

        int totalJugadores = salaUsuarioRepository.countBySala_IdSala(sala.getIdSala());
        if (totalJugadores < sala.getMinJugadores()) {
            throw new IllegalStateException(
                    "Se necesitan al menos " + sala.getMinJugadores() + " jugadores para iniciar");
        }

        sala.setEstadoSala(Sala.EstadoSala.INICIADA);
        salaRepository.save(sala);
    }

    private String generarCodigoUnico() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String codigo;

        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
            }
            codigo = sb.toString();
        } while (salaRepository.existsByCodigoSala(codigo));

        return codigo;
    }
}