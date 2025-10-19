
package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.Menu;
import com.proyecto.analisis.entity.Modulo;
import com.proyecto.analisis.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Menu> guardar(@RequestBody Menu menu, @RequestHeader("Authorization") String token ) {
        return ResponseEntity.ok(service.guardar(menu, token));
    }

    @GetMapping
    public ResponseEntity<List<Menu>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Menu> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> actualizar(@PathVariable Integer id, @RequestBody Menu menu, @RequestHeader("Authorization") String token) {
        return service.actualizar(id, menu, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(menu -> {
                    service.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/modulos")
    public ResponseEntity<List<Modulo>> listarModulosPorMenu() {
        return ResponseEntity.ok(service.listarModulos());
    }
}
