

package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.service.GeneroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/generos")
public class GeneroController {

    private final GeneroService service;

    public GeneroController(GeneroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Genero> guardar(@RequestBody Genero genero, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(genero, token));
    }

    @GetMapping
    public ResponseEntity<List<Genero>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

        @GetMapping("/id/{id}")
    public ResponseEntity<Genero> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(genero -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

     @PutMapping("/{id}")
    public ResponseEntity<Genero> actualizar(@PathVariable Integer id, @RequestBody Genero genero, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, genero, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}