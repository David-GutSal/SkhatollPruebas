package com.skhatoll.backend.controller;

import com.skhatoll.backend.dto.sala.AsignarNarradorRequest;
import com.skhatoll.backend.dto.sala.CrearSalaResponse;
import com.skhatoll.backend.dto.sala.JugadorDto;
import com.skhatoll.backend.dto.sala.UnirseRequest;
import com.skhatoll.backend.service.interfaces.sala.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaController {

    private final ISalaService salaService;

    // -------------------------------------------------------
    // POST /salas/crear
    // Crea una sala y devuelve el código
    // -------------------------------------------------------
    @PostMapping("/crear")
    public ResponseEntity<?> crearSala() {
        try {
            CrearSalaResponse response = salaService.crearSala();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la sala");
        }
    }

    // -------------------------------------------------------
    // POST /salas/unirse
    // Body: { "codigoSala": "ABC123" }
    // -------------------------------------------------------
    @PostMapping("/unirse")
    public ResponseEntity<?> unirse(@RequestBody UnirseRequest request) {
        try {
            salaService.unirse(request);
            return ResponseEntity.ok("Te has unido a la sala correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // GET /salas/{codigo}/jugadores
    // Devuelve la lista de jugadores en la sala
    // -------------------------------------------------------
    @GetMapping("/{codigo}/jugadores")
    public ResponseEntity<?> getJugadores(@PathVariable String codigo) {
        try {
            List<JugadorDto> jugadores = salaService.getJugadores(codigo);
            return ResponseEntity.ok(jugadores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // PUT /salas/{codigo}/narrador
    // Body: { "idUsuario": 1 }
    // Solo el creador puede llamar a este endpoint
    // -------------------------------------------------------
    @PutMapping("/{codigo}/narrador")
    public ResponseEntity<?> asignarNarrador(@PathVariable String codigo,
                                             @RequestBody AsignarNarradorRequest request) {
        try {
            salaService.asignarNarrador(codigo, request);
            return ResponseEntity.ok("Narrador asignado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // POST /salas/{codigo}/iniciar
    // Solo el narrador puede llamar a este endpoint
    // -------------------------------------------------------
    @PostMapping("/{codigo}/iniciar")
    public ResponseEntity<?> iniciarPartida(@PathVariable String codigo) {
        try {
            salaService.iniciarPartida(codigo);
            return ResponseEntity.ok("Partida iniciada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
