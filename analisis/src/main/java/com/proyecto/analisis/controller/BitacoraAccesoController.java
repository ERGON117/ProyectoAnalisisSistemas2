package com.proyecto.analisis.controller;

import com.proyecto.analisis.entity.BitacoraAcceso;
import com.proyecto.analisis.service.BitacoraAccesoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bitacora-accesos")
public class BitacoraAccesoController {

    private final BitacoraAccesoService service;

    public BitacoraAccesoController(BitacoraAccesoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BitacoraAcceso> guardar(@RequestBody BitacoraAcceso bitacora) {
        return ResponseEntity.ok(service.guardar(bitacora));
    }

    @GetMapping("/ultimos/{idUsuario}")
    public ResponseEntity<List<BitacoraAcceso>> ultimosAccesos(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.getUltimosAccesos(idUsuario));
    }

    @GetMapping("/intentos/{idUsuario}")
    public ResponseEntity<Long> contarIntentos(@PathVariable String idUsuario,
                                               @RequestParam LocalDateTime desde) {
        return ResponseEntity.ok(service.contarIntentosRecientes(idUsuario, desde));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<BitacoraAcceso>> accesosPorRango(@RequestParam String idUsuario,
                                                                @RequestParam LocalDateTime desde,
                                                                @RequestParam LocalDateTime hasta) {
        return ResponseEntity.ok(service.getAccesosPorRango(idUsuario, desde, hasta));
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<BitacoraAcceso>> porTipo(@RequestParam String tipo,
                                                        @RequestParam LocalDateTime desde,
                                                        @RequestParam LocalDateTime hasta) {
        return ResponseEntity.ok(service.filtrarPorTipoAcceso(tipo, desde, hasta));
    }
}
