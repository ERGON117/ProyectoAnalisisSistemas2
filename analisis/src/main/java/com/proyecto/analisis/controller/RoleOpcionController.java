package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.service.RoleOpcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-opciones")
public class RoleOpcionController {

    private final RoleOpcionService service;

    public RoleOpcionController(RoleOpcionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoleOpcion> guardar(@RequestBody RoleOpcion roleOpcion) {
        return ResponseEntity.ok(service.guardar(roleOpcion));
    }

}
