package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "EMPRESA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEmpresa")
    private Integer idEmpresa;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Direccion")
    private String direccion;

    @Column(name = "Nit")
    private String nit;

    @Column(name = "PasswordCantidadMayusculas")
    private Integer passwordCantidadMayusculas;

    @Column(name = "PasswordCantidadMinusculas")
    private Integer passwordCantidadMinusculas;

    @Column(name = "PasswordCantidadCaracteresEspeciales")
    private Integer passwordCantidadCaracteresEspeciales;

    @Column(name = "PasswordCantidadCaducidadDias")
    private Integer passwordCantidadCaducidadDias;

    @Column(name = "PasswordLargo")
    private Integer passwordLargo;

    @Column(name = "PasswordIntentosAntesDeBloquear")
    private Integer passwordIntentosAntesDeBloquear;

    @Column(name = "PasswordCantidadNumeros")
    private Integer passwordCantidadNumeros;

    @Column(name = "PasswordCantidadPreguntasValidar")
    private Integer passwordCantidadPreguntasValidar;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;

    //@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    //private List<Sucursal> sucursales;
}
