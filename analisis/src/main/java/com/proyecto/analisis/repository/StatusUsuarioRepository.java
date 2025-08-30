package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.StatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusUsuarioRepository extends JpaRepository<StatusUsuario, Integer> {

    Optional<StatusUsuario> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
