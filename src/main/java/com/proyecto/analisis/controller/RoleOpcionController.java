package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.RoleOpcionDTO;
import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.service.RoleOpcionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/role-opcion")
public class RoleOpcionController {

    @Autowired
    private RoleOpcionService roleOpcionService;

    @PostMapping("/asignar/{idRole}")
    public ResponseEntity<Map<String, String>> asignarOpciones(
            @PathVariable Integer idRole,
            @RequestBody List<RoleOpcionDTO> opciones) {
        roleOpcionService.asignarOpcionesARol(idRole, opciones);
        
        // Respuesta JSON simple
        Map<String, String> response = new HashMap<>();
        response.put("message", "Opciones asignadas correctamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rol/{idRole}")
    public ResponseEntity<List<RoleOpcion>> obtenerOpcionesPorRol(@PathVariable Integer idRole) {
        return ResponseEntity.ok(roleOpcionService.obtenerOpcionesPorRol(idRole));
    }
}