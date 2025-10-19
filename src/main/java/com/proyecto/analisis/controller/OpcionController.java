package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Menu;
import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.service.OpcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/opciones")
public class OpcionController {

    private final OpcionService service;

    public OpcionController(OpcionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Opcion> guardar(@RequestBody Opcion opcion, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(opcion, token));
    }

    @GetMapping
    public ResponseEntity<List<Opcion>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> listarMenus() {
        return ResponseEntity.ok(service.getAllMenus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opcion> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(opcion -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Opcion> actualizar(@PathVariable Integer id, @RequestBody Opcion opcion, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, opcion, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
