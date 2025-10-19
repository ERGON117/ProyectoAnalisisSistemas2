// DTO para resumen de cuentas
package com.proyecto.analisis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoCuentaResumenDTO {
    private Integer idSaldoCuenta;
    private String statusCuentaNombre;
    private String tipoSaldoCuentaNombre;
    private BigDecimal saldoActual;
}