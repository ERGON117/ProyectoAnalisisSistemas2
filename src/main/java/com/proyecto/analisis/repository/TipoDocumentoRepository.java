package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    
    Optional<TipoDocumento> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}