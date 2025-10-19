package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.EstadoCuentaDTO;
import com.proyecto.analisis.service.EstadoCuentaService;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.analisis.service.ReporteEstadoCuentaService;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/estado-cuenta")
@RequiredArgsConstructor
public class EstadoCuentaController {

    private final EstadoCuentaService estadoCuentaService;
    private final ReporteEstadoCuentaService reporteEstadoCuentaService;

    @GetMapping("/consulta")
    public ResponseEntity<EstadoCuentaDTO> consultarEstadoCuenta(
            @RequestParam(required = false) Integer idPersona,
            @RequestParam(required = false) Integer idSaldoCuenta,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        EstadoCuentaDTO dto = estadoCuentaService.generarEstadoCuenta(idPersona, idSaldoCuenta, nombre, apellido, inicio, fin);
        return ResponseEntity.ok(dto);
    }
     @GetMapping("/export/pdf")
    public void exportarPdf(
            @RequestParam(required = false) Integer idPersona,
            @RequestParam(required = false) Integer idSaldoCuenta,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin,
            HttpServletResponse response) throws IOException {

        var estadoCuenta = estadoCuentaService.generarEstadoCuenta(idPersona, idSaldoCuenta, nombre, apellido, inicio, fin);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=estado_cuenta.pdf");

        reporteEstadoCuentaService.generarPdf(estadoCuenta, response.getOutputStream());
    }

    @GetMapping("/export/excel")
    public void exportarExcel(
            @RequestParam(required = false) Integer idPersona,
            @RequestParam(required = false) Integer idSaldoCuenta,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin,
            HttpServletResponse response) throws IOException {

        var estadoCuenta = estadoCuentaService.generarEstadoCuenta(idPersona, idSaldoCuenta, nombre, apellido, inicio, fin);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=estado_cuenta.xlsx");

        reporteEstadoCuentaService.generarExcel(estadoCuenta, response.getOutputStream());
    }
}
