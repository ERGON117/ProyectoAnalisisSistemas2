package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "SALDO_CUENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaldoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
