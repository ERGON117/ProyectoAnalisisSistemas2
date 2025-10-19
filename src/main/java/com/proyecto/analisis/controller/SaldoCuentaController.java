package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.SaldoConsultaDTO;
import com.proyecto.analisis.service.SaldoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/saldos")
public class SaldoCuentaController {

    @Autowired
    private SaldoCuentaService saldoCuentaService;

    @GetMapping("/consulta")
    public ResponseEntity<List<SaldoConsultaDTO>> consultarSaldos(
            @RequestParam(required = false) Integer idPersona,
            @RequestParam(required = false) Integer idSaldoCuenta,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido) {
        
        // Validar que al menos un parámetro de búsqueda esté presente
        if (idPersona == null && idSaldoCuenta == null && 
            (nombre == null || apellido == null)) {
            return ResponseEntity.badRequest().build();
        }
        
        List<SaldoConsultaDTO> saldos = saldoCuentaService.consultarSaldos(idPersona, idSaldoCuenta, nombre, apellido);
        return ResponseEntity.ok(saldos);
    }
}