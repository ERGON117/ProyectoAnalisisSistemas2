package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.service.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Sucursal> guardar(@RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(service.guardar(sucursal));
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<Sucursal>> listarPorEmpresa(@PathVariable Integer idEmpresa) {
        return ResponseEntity.ok(service.listarPorEmpresa(idEmpresa));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Sucursal>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    @GetMapping("/empresa/{idEmpresa}/buscar")
    public ResponseEntity<List<Sucursal>> buscarPorEmpresaYNombre(@PathVariable Integer idEmpresa,
                                                                  @RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorEmpresaYNombre(idEmpresa, nombre));
    }
}
