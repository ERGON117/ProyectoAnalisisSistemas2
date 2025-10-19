package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TIPO_MOVIMIENTO_CXC")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoMovimientoCXC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoMovimientoCXC")
    private Integer idTipoMovimientoCXC;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "OperacionCuentaCorriente")
    private Integer operacionCuentaCorriente; // 1 = Sumar, 2 = Restar

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
