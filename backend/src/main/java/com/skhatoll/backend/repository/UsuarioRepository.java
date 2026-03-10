package com.skhatoll.backend.repository;

import com.skhatoll.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
