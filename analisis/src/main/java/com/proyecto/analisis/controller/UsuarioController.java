package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.guardar(usuario));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        return service.buscarPorCorreo(correo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{idRole}")
    public ResponseEntity<List<Usuario>> listarPorRol(@PathVariable Integer idRole) {
        return ResponseEntity.ok(service.listarPorRol(idRole));
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<Usuario>> listarPorSucursal(@PathVariable Integer idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Usuario>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.listarPorStatus(status));
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Usuario>> listarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(service.listarPorGenero(genero));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<Usuario>> listarRecientes(@RequestParam LocalDateTime desde) {
        return ResponseEntity.ok(service.listarConIngresosRecientes(desde));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarConSesionActiva() {
        return ResponseEntity.ok(service.listarConSesionActiva());
    }
}
