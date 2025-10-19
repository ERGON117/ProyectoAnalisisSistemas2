package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.DocumentoPersona;
import com.proyecto.analisis.entity.DocumentoPersonaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoPersonaRepository extends JpaRepository<DocumentoPersona, DocumentoPersonaId> {
    
    List<DocumentoPersona> findByPersonaIdPersona(Integer idPersona);
    Optional<DocumentoPersona> findByPersonaIdPersonaAndTipoDocumentoIdTipoDocumento(Integer idPersona, Integer idTipoDocumento);
    boolean existsByPersonaIdPersonaAndTipoDocumentoIdTipoDocumento(Integer idPersona, Integer idTipoDocumento);
    void deleteByPersonaIdPersona(Integer idPersona);
}