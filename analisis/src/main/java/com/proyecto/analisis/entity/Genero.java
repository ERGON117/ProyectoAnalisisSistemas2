package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "GENERO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdGenero")
    private Integer idGenero;

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

    //@OneToMany(mappedBy = "genero", cascade = CascadeType.ALL)
    //private List<Usuario> usuarios;
}
