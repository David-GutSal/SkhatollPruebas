package com.skhatoll.backend.repository;

import com.skhatoll.backend.entities.SalaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaUsuarioRepository extends JpaRepository<SalaUsuario, Integer> {

    List<SalaUsuario> findBySala_IdSala(Integer idSala);

    boolean existsBySala_IdSalaAndUsuario_IdUsuario(Integer idSala, Integer idUsuario);

    Optional<SalaUsuario> findBySala_IdSalaAndUsuario_IdUsuario(Integer idSala, Integer idUsuario);

    int countBySala_IdSala(Integer idSala);
}
