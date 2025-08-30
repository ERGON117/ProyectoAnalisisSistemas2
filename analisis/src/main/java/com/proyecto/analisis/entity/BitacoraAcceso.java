package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BITACORA_ACCESO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdBitacoraAcceso")
    private Integer idBitacoraAcceso;

    @Column(name = "IdUsuario")
    private String idUsuario;

    @ManyToOne
    @JoinColumn(name = "IdTipoAcceso", referencedColumnName = "IdTipoAcceso")
    private TipoAcceso tipoAcceso;

    @Column(name = "FechaAcceso")
    private LocalDateTime fechaAcceso;

    @Column(name = "HttpUserAgent")
    private String httpUserAgent;

    @Column(name = "DireccionIp")
    private String direccionIp;

    @Column(name = "Accion")
    private String accion;

    @Column(name = "SistemaOperativo")
    private String sistemaOperativo;

    @Column(name = "Dispositivo")
    private String dispositivo;

    @Column(name = "Browser")
    private String browser;

    @Column(name = "Sesion")
    private String sesion;
}
