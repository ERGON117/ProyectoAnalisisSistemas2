package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.TipoDocumento;
import com.proyecto.analisis.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository repository;
    private final AuthService authService;

    public TipoDocumentoService(TipoDocumentoRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public TipoDocumento guardar(TipoDocumento tipoDocumento, String token) {
        tipoDocumento.setFechaCreacion(LocalDateTime.now());
        tipoDocumento.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(tipoDocumento);
    }

    public List<TipoDocumento> listarTodos() {
        return repository.findAll();
    }

    public Optional<TipoDocumento> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<TipoDocumento> actualizar(Integer id, TipoDocumento tipoDocumentoActualizado, String token) {
        return repository.findById(id).map(tipoDocumentoExistente -> {
            tipoDocumentoExistente.setNombre(tipoDocumentoActualizado.getNombre());

            tipoDocumentoExistente.setFechaModificacion(LocalDateTime.now());
            tipoDocumentoExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(tipoDocumentoExistente);
        });
    }
}