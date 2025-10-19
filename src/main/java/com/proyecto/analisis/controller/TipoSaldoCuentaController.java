package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.TipoSaldoCuenta;
import com.proyecto.analisis.service.TipoSaldoCuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/tipo-saldo-cuenta")
public class TipoSaldoCuentaController {

    private final TipoSaldoCuentaService service;

    public TipoSaldoCuentaController(TipoSaldoCuentaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoSaldoCuenta> guardar(@RequestBody TipoSaldoCuenta tipoSaldoCuenta, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(tipoSaldoCuenta, token));
    }

    @GetMapping
    public ResponseEntity<List<TipoSaldoCuenta>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TipoSaldoCuenta> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoSaldoCuenta> actualizar(@PathVariable Integer id, @RequestBody TipoSaldoCuenta tipoSaldoCuenta, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, tipoSaldoCuenta, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(tipoSaldoCuenta -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}