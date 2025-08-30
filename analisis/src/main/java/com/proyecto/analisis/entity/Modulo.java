package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MODULO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdModulo")
    private Integer idModulo;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "OrdenMenu")
    private Integer ordenMenu;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;

    //@OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    //private List<Menu> menus;
}
