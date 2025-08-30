package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role guardar(Role role) {
        return repository.save(role);
    }

    public List<Role> listarTodos() {
        return repository.findAll();
    }

    public Optional<Role> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}
