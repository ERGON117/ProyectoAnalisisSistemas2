package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALDO_CUENTA_HIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SaldoCuentaHistId.class)
public class SaldoCuentaHist {

    @Id
    @Column(name = "Anio")
    private Integer anio;

    @Id
    @Column(name = "Mes")
    private Integer mes;

    @Id
    @Column(name = "IdSaldoCuenta")
    private Integer idSaldoCuenta;


    @ManyToOne
    @JoinColumn(name = "IdPersona")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "IdStatusCuenta")
    private StatusCuenta statusCuenta;

    @ManyToOne
    @JoinColumn(name = "IdTipoSaldoCuenta")
    private TipoSaldoCuenta tipoSaldoCuenta;

    @Column(name = "SaldoAnterior")
    private BigDecimal saldoAnterior;

    @Column(name = "Debitos")
    private BigDecimal debitos;

    @Column(name = "Creditos")
    private BigDecimal creditos;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
