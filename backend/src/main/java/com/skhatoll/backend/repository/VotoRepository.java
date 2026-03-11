package com.skhatoll.backend.repository;

import com.skhatoll.backend.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Integer> {

    List<Voto> findBySesion_IdSesion(Integer idSesion);

    Optional<Voto> findBySesion_IdSesionAndVotante_IdUsuario(Integer idSesion, Integer idUsuario);

    int countBySesion_IdSesionAndObjetivo_IdUsuario(Integer idSesion, Integer idUsuario);
}
