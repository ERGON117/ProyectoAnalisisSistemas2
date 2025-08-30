package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> listarPorEmpresa(Integer idEmpresa) {
        return repository.findByEmpresa_IdEmpresa(idEmpresa);
    }

    public List<Sucursal> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Sucursal> buscarPorEmpresaYNombre(Integer idEmpresa, String nombre) {
        return repository.findByEmpresa_IdEmpresaAndNombreContainingIgnoreCase(idEmpresa, nombre);
    }

    public boolean existePorEmpresaYNombre(Integer idEmpresa, String nombre) {
        return repository.existsByEmpresa_IdEmpresaAndNombreIgnoreCase(idEmpresa, nombre);
    }

    public Sucursal guardar(Sucursal sucursal) {
        return repository.save(sucursal);
    }
}
