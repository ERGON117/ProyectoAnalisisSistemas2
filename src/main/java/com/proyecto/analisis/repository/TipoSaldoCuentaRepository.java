package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.TipoSaldoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoSaldoCuentaRepository extends JpaRepository<TipoSaldoCuenta, Integer> {
    
    Optional<TipoSaldoCuenta> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}