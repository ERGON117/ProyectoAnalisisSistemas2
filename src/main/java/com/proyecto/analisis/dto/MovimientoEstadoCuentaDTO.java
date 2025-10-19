package com.proyecto.analisis.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoEstadoCuentaDTO {
    private LocalDateTime fechaMovimiento;
    private String tipoMovimiento;
    private String descripcion;
    private BigDecimal cargo;
    private BigDecimal abono;
    private BigDecimal saldoAcumulado;
}
