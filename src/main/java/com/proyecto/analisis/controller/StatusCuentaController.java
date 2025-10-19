package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.StatusCuenta;
import com.proyecto.analisis.service.StatusCuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/status-cuenta")
public class StatusCuentaController {

    private final StatusCuentaService service;

    public StatusCuentaController(StatusCuentaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StatusCuenta> guardar(@RequestBody StatusCuenta statusCuenta, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(statusCuenta, token));
    }

    @GetMapping
    public ResponseEntity<List<StatusCuenta>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StatusCuenta> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusCuenta> actualizar(@PathVariable Integer id, @RequestBody StatusCuenta statusCuenta, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, statusCuenta, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(statusCuenta -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}