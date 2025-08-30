package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Modulo;
import com.proyecto.analisis.service.ModuloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService service;

    public ModuloController(ModuloService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Modulo> guardar(@RequestBody Modulo modulo) {
        return ResponseEntity.ok(service.guardar(modulo));
    }


}
