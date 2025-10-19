package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.EstadoCivil;
import com.proyecto.analisis.repository.EstadoCivilRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstadoCivilService {

    private final EstadoCivilRepository repository;
    private final AuthService authService;

    public EstadoCivilService(EstadoCivilRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public EstadoCivil guardar(EstadoCivil estadoCivil, String token) {
        estadoCivil.setFechaCreacion(LocalDateTime.now());
        estadoCivil.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(estadoCivil);
    }

    public List<EstadoCivil> listarTodos() {
        return repository.findAll();
    }

    public Optional<EstadoCivil> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<EstadoCivil> actualizar(Integer id, EstadoCivil estadoCivilActualizado, String token) {
        return repository.findById(id).map(estadoCivilExistente -> {
            estadoCivilExistente.setNombre(estadoCivilActualizado.getNombre());

            estadoCivilExistente.setFechaModificacion(LocalDateTime.now());
            estadoCivilExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(estadoCivilExistente);
        });
    }
}