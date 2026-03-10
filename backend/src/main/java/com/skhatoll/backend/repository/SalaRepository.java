package com.skhatoll.backend.repository;

import com.skhatoll.backend.entities.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {

    Optional<Sala> findByCodigoSala(String codigoSala);

    boolean existsByCodigoSala(String codigoSala);
}
