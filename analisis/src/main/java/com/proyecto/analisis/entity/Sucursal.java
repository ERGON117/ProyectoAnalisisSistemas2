package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SUCURSAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSucursal")
    private Integer idSucursal;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Direccion")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    private Empresa empresa;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;

    // @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
    // private List<Usuario> usuarios;
}
