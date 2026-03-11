package com.skhatoll.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sala_usuarios", uniqueConstraints = @UniqueConstraint(columnNames = {"id_sala", "id_usuario"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala_usuario")
    private Integer idSalaUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @Column(name = "esta_vivo", nullable = false)
    @Builder.Default
    private Boolean estaVivo = true;

    @Column(name = "muerte_confirmada", nullable = false)
    @Builder.Default
    private Boolean muerteConfirmada = false;
}