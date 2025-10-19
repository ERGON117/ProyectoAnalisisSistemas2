package com.proyecto.analisis.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePersonaDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Integer idGenero;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Integer idEstadoCivil;
    private List<DocumentoDTO> documentos;
}
