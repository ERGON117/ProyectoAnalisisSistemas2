package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    // Buscar empresa por NIT
    Optional<Empresa> findByNit(String nit);

    // Buscar empresa por nombre
    Optional<Empresa> findByNombre(String nombre);

    // Verificar si existe una empresa por NIT
    boolean existsByNit(String nit);
}
