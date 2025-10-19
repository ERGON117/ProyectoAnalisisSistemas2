package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario,  @RequestHeader("Authorization") String token) {
        try {
            Usuario createdUsuario = usuarioService.createUsuario(usuario, token);
            return ResponseEntity.ok(createdUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable String id) {
        try {
            Usuario usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable String id, @RequestBody Usuario usuario,  @RequestHeader("Authorization") String token) {
        try {
            Usuario updatedUsuario = usuarioService.updateUsuario(id, usuario, token);
            return ResponseEntity.ok(updatedUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sucursales")
    public ResponseEntity<List<Sucursal>> getAllSucursales() {
        List<Sucursal> sucursales = usuarioService.getAllSucursales();
        return ResponseEntity.ok(sucursales);
    }


    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = usuarioService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/status-usuarios")
    public ResponseEntity<List<StatusUsuario>> getAllStatusUsuarios() {
        List<StatusUsuario> statusUsuarios = usuarioService.getAllStatusUsuarios();
        return ResponseEntity.ok(statusUsuarios);
    }


    @GetMapping("/generos")
    public ResponseEntity<List<Genero>> getAllGeneros() {
        List<Genero> generos = usuarioService.getAllGeneros();
        return ResponseEntity.ok(generos);
    }
}