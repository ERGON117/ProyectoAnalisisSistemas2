package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.EstadoCivil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoCivilRepository extends JpaRepository<EstadoCivil, Integer> {
    
    Optional<EstadoCivil> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}