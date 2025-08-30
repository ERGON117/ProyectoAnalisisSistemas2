package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.repository.StatusUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusUsuarioService {

    private final StatusUsuarioRepository repository;

    public StatusUsuarioService(StatusUsuarioRepository repository) {
        this.repository = repository;
    }

    public StatusUsuario guardar(StatusUsuario status) {
        return repository.save(status);
    }

    public List<StatusUsuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<StatusUsuario> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}
