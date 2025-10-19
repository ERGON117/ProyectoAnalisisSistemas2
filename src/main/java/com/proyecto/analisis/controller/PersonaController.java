package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.CreatePersonaDTO;
import com.proyecto.analisis.dto.DocumentoDTO;
import com.proyecto.analisis.entity.DocumentoPersona;
import com.proyecto.analisis.entity.Persona;
import com.proyecto.analisis.entity.TipoDocumento;
import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.entity.EstadoCivil;
import com.proyecto.analisis.service.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody CreatePersonaDTO request, 
                                   @RequestHeader("Authorization") String token) {
        try {
            Persona persona = mapToPersona(request);
            List<DocumentoPersona> documentos = mapToDocumentos(request.getDocumentos(), null);
            Persona resultado = personaService.guardar(persona, documentos, token);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
        return ResponseEntity.ok(personaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Integer id) {
        Optional<Persona> personaOpt = personaService.buscarPorId(id);
        if (personaOpt.isPresent()) {
            Persona persona = personaOpt.get();
            List<DocumentoPersona> documentos = personaService.obtenerDocumentosDePersona(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("persona", persona);
            response.put("documentos", documentos);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, 
                                      @RequestBody CreatePersonaDTO request,
                                      @RequestHeader("Authorization") String token) {
        try {
            Persona personaActualizada = mapToPersona(request);
            List<DocumentoPersona> documentos = mapToDocumentos(request.getDocumentos(), null);
            return personaService.actualizar(id, personaActualizada, documentos, token)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return personaService.buscarPorId(id)
                .map(persona -> {
                    personaService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/catalogos")
    public ResponseEntity<Map<String, Object>> obtenerCatalogos() {
        Map<String, Object> catalogos = new HashMap<>();
        catalogos.put("generos", personaService.listarGeneros());
        catalogos.put("estadosCiviles", personaService.listarEstadosCiviles());
        catalogos.put("tiposDocumento", personaService.listarTiposDocumento());
        return ResponseEntity.ok(catalogos);
    }

    private Persona mapToPersona(CreatePersonaDTO dto) {
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setFechaNacimiento(dto.getFechaNacimiento());
        persona.setDireccion(dto.getDireccion());
        persona.setTelefono(dto.getTelefono());
        persona.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Configurar entidades relacionadas por ID
        if (dto.getIdGenero() != null) {
            Genero genero = new Genero();
            genero.setIdGenero(dto.getIdGenero());
            persona.setGenero(genero);
        }
        
        if (dto.getIdEstadoCivil() != null) {
            EstadoCivil estadoCivil = new EstadoCivil();
            estadoCivil.setIdEstadoCivil(dto.getIdEstadoCivil());
            persona.setEstadoCivil(estadoCivil);
        }
        
        return persona;
    }

    private List<DocumentoPersona> mapToDocumentos(List<DocumentoDTO> documentosDTO, Persona persona) {
        if (documentosDTO == null) return null;
        return documentosDTO.stream().map(dto -> {
            DocumentoPersona doc = new DocumentoPersona();
            TipoDocumento tipoDoc = new TipoDocumento();
            tipoDoc.setIdTipoDocumento(dto.getIdTipoDocumento());
            doc.setTipoDocumento(tipoDoc);
            doc.setNoDocumento(dto.getNoDocumento());
            doc.setPersona(persona);
            return doc;
        }).toList();
    }
}