

package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Empresa> guardar(@RequestBody Empresa empresa, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.guardar(empresa, token));
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizar(@PathVariable Integer id, @RequestBody Empresa empresa,  @RequestHeader("Authorization") String token) {
        return service.actualizar(id, empresa, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(empresa -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}