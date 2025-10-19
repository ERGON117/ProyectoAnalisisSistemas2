package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MOVIMIENTO_CUENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMovimientoCuenta")
    private Integer idMovimientoCuenta;

    @ManyToOne
    @JoinColumn(name = "IdSaldoCuenta")
    private SaldoCuenta saldoCuenta;

    @ManyToOne
    @JoinColumn(name = "IdTipoMovimientoCXC")
    private TipoMovimientoCXC tipoMovimientoCXC;

    @Column(name = "FechaMovimiento")
    private LocalDateTime fechaMovimiento;

    @Column(name = "ValorMovimiento")
    private BigDecimal valorMovimiento;

    @Column(name = "ValorMovimientoPagado")
    private BigDecimal valorMovimientoPagado;

    @Column(name = "GeneradoAutomaticamente")
    private Boolean generadoAutomaticamente;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
