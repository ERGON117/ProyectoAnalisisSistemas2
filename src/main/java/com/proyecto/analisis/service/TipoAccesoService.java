package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.TipoAcceso;
import com.proyecto.analisis.repository.TipoAccesoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoAccesoService {

    private final TipoAccesoRepository repository;

    public TipoAccesoService(TipoAccesoRepository repository) {
        this.repository = repository;
    }

    public TipoAcceso guardar(TipoAcceso tipoAcceso) {
        return repository.save(tipoAcceso);
    }

    public List<TipoAcceso> listarTodos() {
        return repository.findAll();
    }

    public Optional<TipoAcceso> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}
