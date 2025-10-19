package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.TipoMovimientoCXC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoMovimientoCXCRRepository extends JpaRepository<TipoMovimientoCXC, Integer> {
    
    Optional<TipoMovimientoCXC> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}