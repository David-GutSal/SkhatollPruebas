package com.skhatoll.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_votacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionVotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Integer idSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoVotacion tipo;

    @Column(name = "ronda", nullable = false)
    private Integer ronda;

    @Column(name = "abierta", nullable = false)
    @Builder.Default
    private Boolean abierta = true;

    @Column(name = "fecha_inicio", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    public enum TipoVotacion {
        DIA, LOBOS, HABILIDAD
    }
}
