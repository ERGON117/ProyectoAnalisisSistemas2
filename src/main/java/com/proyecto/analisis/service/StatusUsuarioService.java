package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.repository.StatusUsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatusUsuarioService {

    private final StatusUsuarioRepository repository;
    private final AuthService authService;

    public StatusUsuarioService(StatusUsuarioRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

  public StatusUsuario guardar(StatusUsuario status, String token) {
        status.setFechaCreacion(LocalDateTime.now());
        status.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(status);
    }

    public List<StatusUsuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<StatusUsuario> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<StatusUsuario> actualizar(Integer id, StatusUsuario statusActualizado, String token) {
        return repository.findById(id).map(statusExistente -> {
            statusExistente.setNombre(statusActualizado.getNombre());
            statusExistente.setFechaModificacion(LocalDateTime.now());
            statusExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(statusExistente);
        });
    }
}