package com.skhatoll.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "salas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer idSala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_narrador", nullable = false)
    private Usuario narrador;

    @Column(name = "codigo_sala", nullable = false, unique = true)
    private String codigoSala;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sala", nullable = false)
    @Builder.Default
    private EstadoSala estadoSala = EstadoSala.CREADA;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_dia", nullable = false)
    @Builder.Default
    private EstadoDia estadoDia = EstadoDia.DIA;

    @Column(name = "ronda_actual", nullable = false)
    @Builder.Default
    private Integer rondaActual = 1;

    @Column(name = "min_jugadores", nullable = false)
    @Builder.Default
    private Integer minJugadores = 2;

    @Column(name = "max_jugadores", nullable = false)
    @Builder.Default
    private Integer maxJugadores = 28;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum EstadoSala {
        CREADA, INICIADA, CERRADA
    }

    public enum EstadoDia {
        DIA, NOCHE
    }
}