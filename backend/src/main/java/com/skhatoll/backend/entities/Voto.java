package com.skhatoll.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "votos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_sesion", "id_votante"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto")
    private Integer idVoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionVotacion sesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_votante", nullable = false)
    private Usuario votante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objetivo", nullable = false)
    private Usuario objetivo;

    @Column(name = "fecha_voto", nullable = false)
    @Builder.Default
    private LocalDateTime fechaVoto = LocalDateTime.now();
}
