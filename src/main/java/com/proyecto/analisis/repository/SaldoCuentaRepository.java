package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Persona;
import com.proyecto.analisis.entity.SaldoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaldoCuentaRepository extends JpaRepository<SaldoCuenta, Integer>, JpaSpecificationExecutor<SaldoCuenta> {
    
    // Método existente
    List<SaldoCuenta> findByPersonaIdPersona(Integer idPersona);
    
    // Nuevo método para buscar por objeto Persona
    List<SaldoCuenta> findByPersona(Persona persona);

    // New method for single result
    Optional<SaldoCuenta> findFirstByPersonaIdPersona(Integer idPersona); // Added for single entity
    Optional<SaldoCuenta> findFirstByPersona(Persona persona); // Added for single entity

}