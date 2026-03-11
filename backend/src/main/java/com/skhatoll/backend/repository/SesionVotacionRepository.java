package com.skhatoll.backend.repository;

import com.skhatoll.backend.entities.SesionVotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionVotacionRepository extends JpaRepository<SesionVotacion, Integer> {

    List<SesionVotacion> findBySala_IdSala(Integer idSala);

    Optional<SesionVotacion> findBySala_IdSalaAndAbiertaTrue(Integer idSala);

    boolean existsBySala_IdSalaAndAbiertaTrue(Integer idSala);
}
