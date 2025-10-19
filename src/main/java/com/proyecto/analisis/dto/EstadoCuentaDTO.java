package com.proyecto.analisis.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCuentaDTO {
    private String nombreCliente;
    private String numeroCuenta;
    private String periodo;
    private List<MovimientoEstadoCuentaDTO> movimientos;
    private BigDecimal totalCargos;
    private BigDecimal totalAbonos;
    private BigDecimal saldoFinal;
}
