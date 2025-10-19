package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;
    private final AuthService authService;


    public RoleService(RoleRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public Role guardar(Role role, String token) {
        role.setFechaCreacion(LocalDateTime.now());
        role.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(role);
    }

    public List<Role> listarTodos() {
        return repository.findAll();
    }

public Optional<Role> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

     public Optional<Role> actualizar(Integer id, Role roleActualizado, String token) {
        return repository.findById(id).map(roleExistente -> {
            roleExistente.setNombre(roleActualizado.getNombre());
            roleExistente.setFechaModificacion(LocalDateTime.now());
            roleExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(roleExistente);
        });
    }
}
