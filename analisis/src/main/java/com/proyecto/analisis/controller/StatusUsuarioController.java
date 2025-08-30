package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.service.StatusUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-usuarios")
public class StatusUsuarioController {

    private final StatusUsuarioService service;

    public StatusUsuarioController(StatusUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StatusUsuario> guardar(@RequestBody StatusUsuario status) {
        return ResponseEntity.ok(service.guardar(status));
    }

    @GetMapping
    public ResponseEntity<List<StatusUsuario>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<StatusUsuario> buscarPorNombre(@PathVariable String nombre) {
        return service.buscarPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
