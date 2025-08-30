package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.service.GeneroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    private final GeneroService service;

    public GeneroController(GeneroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Genero> guardar(@RequestBody Genero genero) {
        return ResponseEntity.ok(service.guardar(genero));
    }

    @GetMapping
    public ResponseEntity<List<Genero>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Genero> buscarPorNombre(@PathVariable String nombre) {
        return service.buscarPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
