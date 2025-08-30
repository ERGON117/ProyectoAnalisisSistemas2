package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "OPCION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdOpcion")
    private Integer idOpcion;

    @ManyToOne
    @JoinColumn(name = "IdMenu", referencedColumnName = "IdMenu")
    private Menu menu;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "OrdenMenu")
    private Integer ordenMenu;

    @Column(name = "Pagina")
    private String pagina;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;

    // @OneToMany(mappedBy = "opcion", cascade = CascadeType.ALL)
    // private List<RoleOpcion> roleOpciones;
}
