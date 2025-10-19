

package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.service.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Sucursal> guardar(@RequestBody Sucursal sucursal, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(sucursal, token));
    }

    @GetMapping
    public ResponseEntity<List<Sucursal>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Sucursal> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Integer id, @RequestBody Sucursal sucursal, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, sucursal, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(sucursal -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = service.getAllEmpresas();
        return ResponseEntity.ok(empresas);
    }
}
