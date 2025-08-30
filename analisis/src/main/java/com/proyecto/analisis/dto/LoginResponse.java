package com.proyecto.analisis.dto;

import com.proyecto.analisis.entity.Usuario;

import lombok.*;

@Data
@Builder // <-- Esto habilita el método builder()
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String mensaje;
}
