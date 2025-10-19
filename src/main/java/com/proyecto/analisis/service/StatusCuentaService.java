package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.StatusCuenta;
import com.proyecto.analisis.repository.StatusCuentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatusCuentaService {

    private final StatusCuentaRepository repository;
    private final AuthService authService;

    public StatusCuentaService(StatusCuentaRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public StatusCuenta guardar(StatusCuenta statusCuenta, String token) {
        statusCuenta.setFechaCreacion(LocalDateTime.now());
        statusCuenta.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(statusCuenta);
    }

    public List<StatusCuenta> listarTodos() {
        return repository.findAll();
    }

    public Optional<StatusCuenta> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<StatusCuenta> actualizar(Integer id, StatusCuenta statusCuentaActualizada, String token) {
        return repository.findById(id).map(statusCuentaExistente -> {
            statusCuentaExistente.setNombre(statusCuentaActualizada.getNombre());

            statusCuentaExistente.setFechaModificacion(LocalDateTime.now());
            statusCuentaExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(statusCuentaExistente);
        });
    }
}