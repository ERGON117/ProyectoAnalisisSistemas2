package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.repository.EmpresaRepository;
import com.proyecto.analisis.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SucursalService {

    private final EmpresaRepository empresaRepository;

    private final SucursalRepository repository;

    private final AuthService authService;

    public SucursalService(SucursalRepository repository, EmpresaRepository empresaRepository, AuthService authService) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.authService = authService;
    }

    public Sucursal guardar(Sucursal sucursal, String token) {
        sucursal.setFechaCreacion(LocalDateTime.now());
        sucursal.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(sucursal);
    }

    public List<Sucursal> listarTodos() {
        return repository.findAll();
    }

    public Optional<Sucursal> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Sucursal> actualizar(Integer id, Sucursal sucursalActualizada, String token) {
        return repository.findById(id).map(sucursalExistente -> {
            sucursalExistente.setNombre(sucursalActualizada.getNombre());
            sucursalExistente.setDireccion(sucursalActualizada.getDireccion());
            sucursalExistente.setEmpresa(sucursalActualizada.getEmpresa());
            sucursalExistente.setFechaModificacion(LocalDateTime.now());
            sucursalExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(sucursalExistente);
        });
    }

    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }
}



