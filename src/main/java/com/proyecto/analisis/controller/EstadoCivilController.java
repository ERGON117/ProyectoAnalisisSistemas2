package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.EstadoCivil;
import com.proyecto.analisis.service.EstadoCivilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/estado-civil")
public class EstadoCivilController {

    private final EstadoCivilService service;

    public EstadoCivilController(EstadoCivilService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EstadoCivil> guardar(@RequestBody EstadoCivil estadoCivil, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(estadoCivil, token));
    }

    @GetMapping
    public ResponseEntity<List<EstadoCivil>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EstadoCivil> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoCivil> actualizar(@PathVariable Integer id, @RequestBody EstadoCivil estadoCivil, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, estadoCivil, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(estadoCivil -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}