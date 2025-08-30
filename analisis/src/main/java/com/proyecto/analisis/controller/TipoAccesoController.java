package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.TipoAcceso;
import com.proyecto.analisis.service.TipoAccesoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-acceso")
public class TipoAccesoController {

    private final TipoAccesoService service;

    public TipoAccesoController(TipoAccesoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoAcceso> guardar(@RequestBody TipoAcceso tipo) {
        return ResponseEntity.ok(service.guardar(tipo));
    }

    @GetMapping
    public ResponseEntity<List<TipoAcceso>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<TipoAcceso> buscarPorNombre(@PathVariable String nombre) {
        return service.buscarPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
