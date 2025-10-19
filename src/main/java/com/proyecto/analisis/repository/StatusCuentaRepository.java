package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.StatusCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCuentaRepository extends JpaRepository<StatusCuenta, Integer> {
    
    Optional<StatusCuenta> findByNombre(String nombre);
    
    @Query("SELECT s FROM StatusCuenta s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Optional<StatusCuenta> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    boolean existsByNombre(String nombre);
}