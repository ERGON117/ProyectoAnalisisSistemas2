package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.TipoSaldoCuenta;
import com.proyecto.analisis.repository.TipoSaldoCuentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TipoSaldoCuentaService {

    private final TipoSaldoCuentaRepository repository;
    private final AuthService authService;

    public TipoSaldoCuentaService(TipoSaldoCuentaRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public TipoSaldoCuenta guardar(TipoSaldoCuenta tipoSaldoCuenta, String token) {
        tipoSaldoCuenta.setFechaCreacion(LocalDateTime.now());
        tipoSaldoCuenta.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(tipoSaldoCuenta);
    }

    public List<TipoSaldoCuenta> listarTodos() {
        return repository.findAll();
    }

    public Optional<TipoSaldoCuenta> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<TipoSaldoCuenta> actualizar(Integer id, TipoSaldoCuenta tipoSaldoCuentaActualizado, String token) {
        return repository.findById(id).map(tipoSaldoCuentaExistente -> {
            tipoSaldoCuentaExistente.setNombre(tipoSaldoCuentaActualizado.getNombre());

            tipoSaldoCuentaExistente.setFechaModificacion(LocalDateTime.now());
            tipoSaldoCuentaExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(tipoSaldoCuentaExistente);
        });
    }
}