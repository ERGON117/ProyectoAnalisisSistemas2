package com.proyecto.analisis.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCuentaDTO {

    private Integer idPersona;

    private Integer idStatusCuenta;

    private Integer idTipoSaldoCuenta;

    private BigDecimal saldoAnterior;

    private BigDecimal debitos;

    private BigDecimal creditos;
}