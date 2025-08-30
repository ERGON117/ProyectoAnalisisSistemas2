package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.entity.RoleOpcionId;
import com.proyecto.analisis.repository.RoleOpcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleOpcionService {

    private final RoleOpcionRepository repository;

    public RoleOpcionService(RoleOpcionRepository repository) {
        this.repository = repository;
    }
   
    public RoleOpcion guardar(RoleOpcion roleOpcion) {
        return repository.save(roleOpcion);
    }
}
