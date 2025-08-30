package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @Column(name = "IdUsuario")
    private String idUsuario;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Apellido")
    private String apellido;

    @Column(name = "FechaNacimiento")
    private Date fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "IdStatusUsuario", referencedColumnName = "IdStatusUsuario")
    private StatusUsuario statusUsuario;

    @Column(name = "Password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "IdGenero", referencedColumnName = "IdGenero")
    private Genero genero;

    @Column(name = "UltimaFechaIngreso")
    private LocalDateTime ultimaFechaIngreso;

    @Column(name = "IntentosDeAcceso")
    private Integer intentosDeAcceso;

    @Column(name = "SesionActual")
    private String sesionActual;

    @Column(name = "UltimaFechaCambioPassword")
    private LocalDateTime ultimaFechaCambioPassword;

    @Column(name = "CorreoElectronico")
    private String correoElectronico;

    @Column(name = "RequiereCambiarPassword")
    private Integer requiereCambiarPassword;

    @Lob
    @Column(name = "Fotografia")
    private byte[] fotografia;

    @Column(name = "TelefonoMovil")
    private String telefonoMovil;

    @ManyToOne
    @JoinColumn(name = "IdSucursal", referencedColumnName = "IdSucursal")
    private Sucursal sucursal;

    @Column(name = "Pregunta")
    private String pregunta;

    @Column(name = "Respuesta")
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "IdRole", referencedColumnName = "IdRole")
    private Role role;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
