package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.*;
import com.proyecto.analisis.entity.DocumentoPersona;
import com.proyecto.analisis.entity.SaldoCuenta;
import com.proyecto.analisis.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "http://localhost:4200")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<SaldoCuenta> createCuenta(@RequestBody CreateCuentaDTO dto, @RequestHeader("Authorization") String token) {
        try {
            SaldoCuenta cuenta = cuentaService.createCuenta(dto, token);
            return ResponseEntity.ok(cuenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaldoCuenta> updateCuenta(@PathVariable Integer id, @RequestBody CreateCuentaDTO dto, @RequestHeader("Authorization") String token) {
        try {
            SaldoCuenta cuenta = cuentaService.updateCuenta(id, dto, token);
            return ResponseEntity.ok(cuenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // NUEVO: Listar todas las cuentas
    @GetMapping
    public ResponseEntity<List<SaldoCuenta>> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.getAllCuentas());
    }

    // NUEVO: Obtener cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<SaldoCuenta> getCuentaById(@PathVariable Integer id) {
        return cuentaService.getCuentaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NUEVO: Eliminar cuenta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Integer id) {
        try {
            cuentaService.deleteCuenta(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<SaldoCuenta>> getCuentasByPersona(@PathVariable Integer idPersona) {
        return ResponseEntity.ok(cuentaService.getCuentasByPersona(idPersona));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> getSaldoActual(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(cuentaService.getSaldoActual(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/persona/{idPersona}/documentos")
    public ResponseEntity<List<DocumentoPersona>> getDocumentosByPersona(@PathVariable Integer idPersona) {
        return ResponseEntity.ok(cuentaService.getDocumentosByPersona(idPersona));
    }

    // NUEVO: Endpoint para catálogos
    @GetMapping("/catalogos")
    public ResponseEntity<CatologosCuentasDTO> getCatalogos() {
        return ResponseEntity.ok(cuentaService.getCatalogos());
    }

    // NUEVO: Verificar si persona tiene cuentas
    @GetMapping("/persona/{idPersona}/existe")
    public ResponseEntity<Boolean> personaTieneCuentas(@PathVariable Integer idPersona) {
        return ResponseEntity.ok(cuentaService.personaTieneCuentas(idPersona));
    }
}