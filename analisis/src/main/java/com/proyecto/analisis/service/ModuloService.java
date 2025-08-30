package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Modulo;
import com.proyecto.analisis.repository.ModuloRepositori;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuloService {

    private final ModuloRepositori repository;

    public ModuloService(ModuloRepositori repository) {
        this.repository = repository;
    }

    public Modulo guardar(Modulo modulo) {
        return repository.save(modulo);
    }
    }

