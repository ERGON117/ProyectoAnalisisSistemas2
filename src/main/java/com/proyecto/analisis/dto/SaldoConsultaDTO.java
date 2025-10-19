package com.proyecto.analisis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaldoConsultaDTO {
    private Integer idSaldoCuenta;
    private Integer idPersona;
    private String nombrePersona;
    private String apellidoPersona;
    private Integer idTipoSaldoCuenta;
    private String nombreTipo;
    private Integer idStatusCuenta;
    private String nombreStatus;
    private BigDecimal saldoInicial;
    private BigDecimal cargos;
    private BigDecimal abonos;
    private BigDecimal saldoFinal;
}