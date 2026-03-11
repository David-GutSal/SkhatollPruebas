package com.skhatoll.backend.controller;

import com.skhatoll.backend.dto.partida.AbrirVotacionRequest;
import com.skhatoll.backend.dto.partida.ResultadoVotacionDto;
import com.skhatoll.backend.dto.partida.VotarRequest;
import com.skhatoll.backend.entities.SesionVotacion;
import com.skhatoll.backend.service.interfaces.partida.IPartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partida")
@RequiredArgsConstructor
public class PartidaController {

    private final IPartidaService partidaService;

    // -------------------------------------------------------
    // PUT /partida/{codigo}/fase
    // Alterna entre DÍA y NOCHE
    // -------------------------------------------------------
    @PutMapping("/{codigo}/fase")
    public ResponseEntity<?> cambiarFase(@PathVariable String codigo) {
        try {
            partidaService.cambiarFase(codigo);
            return ResponseEntity.ok("Fase cambiada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // POST /partida/{codigo}/votacion/abrir
    // Body: { "tipo": "DIA" | "LOBOS" | "HABILIDAD" }
    // -------------------------------------------------------
    @PostMapping("/{codigo}/votacion/abrir")
    public ResponseEntity<?> abrirVotacion(@PathVariable String codigo,
                                           @RequestBody AbrirVotacionRequest request) {
        try {
            SesionVotacion sesion = partidaService.abrirVotacion(codigo, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(sesion.getIdSesion());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // PUT /partida/{codigo}/votacion/{idSesion}/cerrar
    // -------------------------------------------------------
    @PutMapping("/{codigo}/votacion/{idSesion}/cerrar")
    public ResponseEntity<?> cerrarVotacion(@PathVariable String codigo,
                                            @PathVariable Integer idSesion) {
        try {
            ResultadoVotacionDto resultado = partidaService.cerrarVotacion(codigo, idSesion);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // POST /partida/{codigo}/votar
    // Body: { "idObjetivo": 1 }
    // -------------------------------------------------------
    @PostMapping("/{codigo}/votar")
    public ResponseEntity<?> votar(@PathVariable String codigo,
                                   @RequestBody VotarRequest request) {
        try {
            partidaService.votar(codigo, request);
            return ResponseEntity.ok("Voto registrado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // PUT /partida/{codigo}/jugador/{idUsuario}/confirmar-muerte
    // -------------------------------------------------------
    @PutMapping("/{codigo}/jugador/{idUsuario}/confirmar-muerte")
    public ResponseEntity<?> confirmarMuerte(@PathVariable String codigo,
                                             @PathVariable Integer idUsuario) {
        try {
            partidaService.confirmarMuerte(codigo, idUsuario);
            return ResponseEntity.ok("Muerte confirmada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
