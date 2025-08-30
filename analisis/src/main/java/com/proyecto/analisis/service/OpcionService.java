package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.repository.OpcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcionService {

    private final OpcionRepository repository;

    public OpcionService(OpcionRepository repository) {
        this.repository = repository;
    }
}
