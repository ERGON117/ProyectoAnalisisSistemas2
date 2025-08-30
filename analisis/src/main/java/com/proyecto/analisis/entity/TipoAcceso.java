package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TIPO_ACCESO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoAcceso")
    private Integer idTipoAcceso;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;

    // @OneToMany(mappedBy = "tipoAcceso", cascade = CascadeType.ALL)
    // private List<BitacoraAcceso> bitacoras;
}
