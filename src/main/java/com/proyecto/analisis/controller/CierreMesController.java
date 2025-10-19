package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.PeriodoCierreMes;
import com.proyecto.analisis.service.CierreMesService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/cierre-mes")
@Slf4j
public class CierreMesController {

    @Autowired
    private CierreMesService cierreMesService;

    @PostMapping("/cerrar")
    public ResponseEntity<Map<String, Object>> cerrarMes(@RequestBody CierreMesRequest request, @RequestHeader("Authorization") String token) {
        try {
            
            log.info("Iniciando cierre de mes para año: {}, mes: {}", request.getAnio(), request.getMes());
            
            cierreMesService.cerrarMes(request.getAnio(), request.getMes(), token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cierre de mes completado exitosamente para " + request.getMes() + "/" + request.getAnio());
            response.put("anio", request.getAnio());
            response.put("mes", request.getMes());
            
            log.info("Cierre de mes completado exitosamente para año: {}, mes: {}", request.getAnio(), request.getMes());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error al cerrar mes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


   

    @Data
    public static class CierreMesRequest {
        private Integer anio;
        private Integer mes;
    }
}