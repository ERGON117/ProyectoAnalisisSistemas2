package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DOCUMENTO_PERSONA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(DocumentoPersonaId.class)
public class DocumentoPersona {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdTipoDocumento", referencedColumnName = "IdTipoDocumento")
    private TipoDocumento tipoDocumento;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdPersona", referencedColumnName = "IdPersona")
    private Persona persona;

    @Column(name = "NoDocumento")
    private String noDocumento;

    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioCreacion")
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion")
    private String usuarioModificacion;
}
