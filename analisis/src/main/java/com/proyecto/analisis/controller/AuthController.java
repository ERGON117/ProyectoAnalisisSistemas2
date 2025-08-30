package com.proyecto.analisis.controller;

import com.proyecto.analisis.dto.MenuResponse;
import com.proyecto.analisis.service.MenuService;
import com.proyecto.analisis.dto.LoginRequest;
import com.proyecto.analisis.dto.UserAgentParser;
import com.proyecto.analisis.dto.ChangePasswordRequest;
import com.proyecto.analisis.exception.AuthResponse;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MenuService menuService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        String sistemaOperativo = UserAgentParser.getSistemaOperativo(userAgent);
        String dispositivo = UserAgentParser.getDispositivo(userAgent);
        String browser = UserAgentParser.getNavegador(userAgent);
        AuthResponse response = authService.login(request, ip, userAgent, sistemaOperativo, dispositivo, browser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
        AuthResponse response = authService.logout(token);
        return ResponseEntity.ok(response);
    }

   @GetMapping("/menu")
    public ResponseEntity<List<MenuResponse>> getMenuStructure(@RequestHeader("Authorization") String token) {
        List<MenuResponse> menuStructure = menuService.getMenuStructure(token);
        return ResponseEntity.ok(menuStructure);
    }

    @GetMapping("/profile")
    public ResponseEntity<Usuario> getProfile(@RequestHeader("Authorization") String token) {
        Usuario usuario = authService.getProfile(token);
        usuario.setPassword(null);
        usuario.setFotografia(null);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword(@RequestHeader("Authorization") String token, 
                                                      @RequestBody ChangePasswordRequest request) {
        AuthResponse response = authService.changePassword(token, request);
        return ResponseEntity.ok(response);
    }
}