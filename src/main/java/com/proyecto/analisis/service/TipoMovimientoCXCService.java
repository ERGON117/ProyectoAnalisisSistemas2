package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.TipoMovimientoCXC;
import com.proyecto.analisis.repository.TipoMovimientoCXCRRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TipoMovimientoCXCService {

    private final TipoMovimientoCXCRRepository repository;
    private final AuthService authService;

    public TipoMovimientoCXCService(TipoMovimientoCXCRRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public TipoMovimientoCXC guardar(TipoMovimientoCXC tipoMovimientoCXC, String token) {
        tipoMovimientoCXC.setFechaCreacion(LocalDateTime.now());
        tipoMovimientoCXC.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(tipoMovimientoCXC);
    }

    public List<TipoMovimientoCXC> listarTodos() {
        return repository.findAll();
    }

    public Optional<TipoMovimientoCXC> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<TipoMovimientoCXC> actualizar(Integer id, TipoMovimientoCXC tipoMovimientoCXCActualizado, String token) {
        return repository.findById(id).map(tipoMovimientoCXCExistente -> {
            tipoMovimientoCXCExistente.setNombre(tipoMovimientoCXCActualizado.getNombre());
            tipoMovimientoCXCExistente.setOperacionCuentaCorriente(tipoMovimientoCXCActualizado.getOperacionCuentaCorriente());

            tipoMovimientoCXCExistente.setFechaModificacion(LocalDateTime.now());
            tipoMovimientoCXCExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(tipoMovimientoCXCExistente);
        });
    }
}