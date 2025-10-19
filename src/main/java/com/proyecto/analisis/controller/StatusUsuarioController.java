package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.service.StatusUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class StatusUsuarioController {

    private final StatusUsuarioService service;

    public StatusUsuarioController(StatusUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StatusUsuario> guardar(@RequestBody StatusUsuario status, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(status, token));
    }

    @GetMapping
    public ResponseEntity<List<StatusUsuario>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

     @GetMapping("/id/{id}")
    public ResponseEntity<StatusUsuario> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(status -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusUsuario> actualizar(@PathVariable Integer id, @RequestBody StatusUsuario status, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, status, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
