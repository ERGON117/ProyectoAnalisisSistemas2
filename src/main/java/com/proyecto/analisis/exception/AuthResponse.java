package com.proyecto.analisis.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private boolean success;
    private String mensaje;
    private String token;
    private boolean requiereCambioPassword;
}
