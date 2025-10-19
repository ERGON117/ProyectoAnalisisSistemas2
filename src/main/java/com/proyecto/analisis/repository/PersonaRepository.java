// PersonaRepository (ya proporcionado anteriormente)
package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    Optional<Persona> findByCorreoElectronico(String correoElectronico);
    boolean existsByCorreoElectronico(String correoElectronico);
    List<Persona> findByNombreAndApellido(String nombre, String apellido);
    Optional<Persona> findFirstByNombreAndApellido(String nombre, String apellido); // Added for single entity
}