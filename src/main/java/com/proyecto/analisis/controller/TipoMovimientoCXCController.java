package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.TipoMovimientoCXC;
import com.proyecto.analisis.service.TipoMovimientoCXCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/tipo-movimiento-cxc")
public class TipoMovimientoCXCController {

    private final TipoMovimientoCXCService service;

    public TipoMovimientoCXCController(TipoMovimientoCXCService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoMovimientoCXC> guardar(@RequestBody TipoMovimientoCXC tipoMovimientoCXC, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(tipoMovimientoCXC, token));
    }

    @GetMapping
    public ResponseEntity<List<TipoMovimientoCXC>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TipoMovimientoCXC> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoMovimientoCXC> actualizar(@PathVariable Integer id, @RequestBody TipoMovimientoCXC tipoMovimientoCXC, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, tipoMovimientoCXC, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(tipoMovimientoCXC -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}