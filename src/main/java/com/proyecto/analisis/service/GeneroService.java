package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GeneroService {

    private final GeneroRepository repository;
    private final AuthService authService;

    public GeneroService(GeneroRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }


    public Genero guardar(Genero genero, String token) {
        genero.setFechaCreacion(LocalDateTime.now());
        genero.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(genero);
    }


    public List<Genero> listarTodos() {
        return repository.findAll();
    }

        public Optional<Genero> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

     public Optional<Genero> actualizar(Integer id, Genero generoActualizado, String token) {
        return repository.findById(id).map(generoExistente -> {
            generoExistente.setNombre(generoActualizado.getNombre());
            generoExistente.setFechaModificacion(LocalDateTime.now());
            generoExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(generoExistente);
        });
    }
}


