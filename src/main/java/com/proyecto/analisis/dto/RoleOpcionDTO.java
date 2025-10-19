package com.proyecto.analisis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOpcionDTO {
    private Integer idRole;
    private Integer idOpcion;
    private Boolean alta;
    private Boolean baja;
    private Boolean cambio;
    private Boolean imprimir;
    private Boolean exportar;
}