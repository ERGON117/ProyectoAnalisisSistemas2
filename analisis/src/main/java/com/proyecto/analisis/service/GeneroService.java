package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroService {

    private final GeneroRepository repository;

    public GeneroService(GeneroRepository repository) {
        this.repository = repository;
    }

    public Genero guardar(Genero genero) {
        return repository.save(genero);
    }

    public List<Genero> listarTodos() {
        return repository.findAll();
    }

    public Optional<Genero> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}
