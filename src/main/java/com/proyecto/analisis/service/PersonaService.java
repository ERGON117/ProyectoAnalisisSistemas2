package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.*;
import com.proyecto.analisis.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final DocumentoPersonaRepository documentoPersonaRepository;
    private final GeneroRepository generoRepository;
    private final EstadoCivilRepository estadoCivilRepository;
    private final AuthService authService;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public PersonaService(PersonaRepository personaRepository, 
                         DocumentoPersonaRepository documentoPersonaRepository,
                         GeneroRepository generoRepository,
                         EstadoCivilRepository estadoCivilRepository,
                         AuthService authService,
                         TipoDocumentoRepository tipoDocumentoRepository) {
        this.personaRepository = personaRepository;
        this.documentoPersonaRepository = documentoPersonaRepository;
        this.generoRepository = generoRepository;
        this.estadoCivilRepository = estadoCivilRepository;
        this.authService = authService;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public Persona guardar(Persona persona, List<DocumentoPersona> documentos, String token) {
        // Validar y cargar entidades relacionadas
        if (persona.getGenero() != null && persona.getGenero().getIdGenero() != null) {
            Genero genero = generoRepository.findById(persona.getGenero().getIdGenero())
                    .orElseThrow(() -> new RuntimeException("Género no encontrado"));
            persona.setGenero(genero);
        }
        
        if (persona.getEstadoCivil() != null && persona.getEstadoCivil().getIdEstadoCivil() != null) {
            EstadoCivil estadoCivil = estadoCivilRepository.findById(persona.getEstadoCivil().getIdEstadoCivil())
                    .orElseThrow(() -> new RuntimeException("Estado civil no encontrado"));
            persona.setEstadoCivil(estadoCivil);
        }
        
        persona.setFechaCreacion(LocalDateTime.now());
        persona.setUsuarioCreacion(authService.getCurrentUserId(token));
        
        // Guardar persona
        Persona personaGuardada = personaRepository.save(persona);
        
        // Guardar documentos asociados
        if (documentos != null) {
            for (DocumentoPersona doc : documentos) {
                doc.setPersona(personaGuardada);
                doc.setFechaCreacion(LocalDateTime.now());
                doc.setUsuarioCreacion(authService.getCurrentUserId(token));
                documentoPersonaRepository.save(doc);
            }
        }
        
        return personaGuardada;
    }

    public List<Persona> listarTodos() {
        return personaRepository.findAll();
    }

    public Optional<Persona> buscarPorId(Integer id) {
        return personaRepository.findById(id);
    }

    public void eliminar(Integer id) {
        // Eliminar documentos asociados primero
        documentoPersonaRepository.deleteByPersonaIdPersona(id);
        // Eliminar persona
        personaRepository.deleteById(id);
    }

    public Optional<Persona> actualizar(Integer id, Persona personaActualizada, List<DocumentoPersona> documentosActualizados, String token) {
        return personaRepository.findById(id).map(personaExistente -> {
            // Validar y cargar entidades relacionadas
            if (personaActualizada.getGenero() != null && personaActualizada.getGenero().getIdGenero() != null) {
                Genero genero = generoRepository.findById(personaActualizada.getGenero().getIdGenero())
                        .orElseThrow(() -> new RuntimeException("Género no encontrado"));
                personaExistente.setGenero(genero);
            }
            
            if (personaActualizada.getEstadoCivil() != null && personaActualizada.getEstadoCivil().getIdEstadoCivil() != null) {
                EstadoCivil estadoCivil = estadoCivilRepository.findById(personaActualizada.getEstadoCivil().getIdEstadoCivil())
                        .orElseThrow(() -> new RuntimeException("Estado civil no encontrado"));
                personaExistente.setEstadoCivil(estadoCivil);
            }
            
            // Actualizar campos de persona
            personaExistente.setNombre(personaActualizada.getNombre());
            personaExistente.setApellido(personaActualizada.getApellido());
            personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
            personaExistente.setDireccion(personaActualizada.getDireccion());
            personaExistente.setTelefono(personaActualizada.getTelefono());
            personaExistente.setCorreoElectronico(personaActualizada.getCorreoElectronico());

            personaExistente.setFechaModificacion(LocalDateTime.now());
            personaExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            
            // Guardar persona actualizada
            Persona personaGuardada = personaRepository.save(personaExistente);
            
            // Eliminar documentos existentes
            documentoPersonaRepository.deleteByPersonaIdPersona(id);
            
            // Guardar nuevos documentos
            if (documentosActualizados != null) {
                for (DocumentoPersona doc : documentosActualizados) {
                    doc.setPersona(personaGuardada);
                    doc.setFechaModificacion(LocalDateTime.now());
                    doc.setUsuarioModificacion(authService.getCurrentUserId(token));
                    documentoPersonaRepository.save(doc);
                }
            }
            
            return personaGuardada;
        });
    }
    
    public List<DocumentoPersona> obtenerDocumentosDePersona(Integer idPersona) {
        return documentoPersonaRepository.findByPersonaIdPersona(idPersona);
    }

    public List<Genero> listarGeneros() {
        return generoRepository.findAll();
    }

    public List<EstadoCivil> listarEstadosCiviles() {
        return estadoCivilRepository.findAll();
    }

    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }
}