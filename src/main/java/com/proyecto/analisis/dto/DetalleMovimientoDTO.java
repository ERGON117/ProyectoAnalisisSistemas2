package com.proyecto.analisis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DetalleMovimientoDTO {
    private LocalDate fechaMovimiento;
    private String tipoMovimiento;
    private String descripcion;
    private BigDecimal cargo;
    private BigDecimal abono;
    private BigDecimal saldoAcumulado;
}