package com.proyecto.analisis.controller;
import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Role> guardar(@RequestBody Role role, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(role, token));
    }

    @GetMapping
    public ResponseEntity<List<Role>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Role> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(role -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

     @PutMapping("/{id}")
    public ResponseEntity<Role> actualizar(@PathVariable Integer id, @RequestBody Role role, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, role, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

