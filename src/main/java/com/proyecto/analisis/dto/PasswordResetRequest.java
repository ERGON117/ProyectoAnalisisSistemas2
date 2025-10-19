package com.proyecto.analisis.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String correoElectronico;
    private String respuesta;
    private String newPassword;
}