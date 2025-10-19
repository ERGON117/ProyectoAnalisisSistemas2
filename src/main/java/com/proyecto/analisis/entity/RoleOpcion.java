package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ROLE_OPCION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RoleOpcionId.class)
public class RoleOpcion {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdRole", referencedColumnName = "IdRole")
    private Role role;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdOpcion", referencedColumnName = "IdOpcion")
    private Opcion opcion;

    @Column(name = "Alta")
    private Boolean alta;

    @Column(name = "Baja")
    private Boolean baja;

    @Column(name = "Cambio")
    private Boolean cambio;

    @Column(name = "Imprimir")
    private Boolean imprimir;

    @Column(name = "Exportar")
    private Boolean exportar;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
