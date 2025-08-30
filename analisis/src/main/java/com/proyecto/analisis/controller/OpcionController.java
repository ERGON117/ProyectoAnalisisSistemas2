package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.service.OpcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones")
public class OpcionController {

    private final OpcionService service;

    public OpcionController(OpcionService service) {
        this.service = service;
    }


}
