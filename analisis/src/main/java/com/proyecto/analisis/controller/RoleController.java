package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Role> guardar(@RequestBody Role role) {
        return ResponseEntity.ok(service.guardar(role));
    }

    @GetMapping
    public ResponseEntity<List<Role>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Role> buscarPorNombre(@PathVariable String nombre) {
        return service.buscarPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
