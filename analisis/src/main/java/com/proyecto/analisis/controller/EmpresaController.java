package com.proyecto.analisis.controller;


import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public List<Empresa> listarTodas() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerPorId(@PathVariable Integer id) {
        return empresaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Empresa crear(@RequestBody Empresa empresa) {
        empresa.setFechaCreacion(java.time.LocalDateTime.now());
        empresa.setUsuarioCreacion("system"); // Puedes cambiar esto según usuario logueado
        return empresaService.guardar(empresa);
    }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Integer id, @RequestBody Empresa empresa) {
        empresa.setFechaModificacion(java.time.LocalDateTime.now());
        empresa.setUsuarioModificacion("system"); // Puedes cambiar según usuario logueado
        return empresaService.actualizar(id, empresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

