package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Modulo;
import com.proyecto.analisis.repository.ModuloRepositori;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ModuloService {

    private final ModuloRepositori repository;
    private final AuthService authService;

    public ModuloService(ModuloRepositori repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public List<Modulo> listarTodos() {
        return repository.findAll();
    }

    public Modulo guardar(Modulo modulo, String token) {
        modulo.setFechaCreacion(LocalDateTime.now());
        modulo.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(modulo);
    }

    public Optional<Modulo> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Modulo> actualizar(Integer id, Modulo moduloActualizado, String token) {
        return repository.findById(id).map(moduloExistente -> {
            moduloExistente.setNombre(moduloActualizado.getNombre());
            moduloExistente.setOrdenMenu(moduloActualizado.getOrdenMenu());
            moduloExistente.setFechaModificacion(LocalDateTime.now());
            moduloExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(moduloExistente);
        });
    }
}
