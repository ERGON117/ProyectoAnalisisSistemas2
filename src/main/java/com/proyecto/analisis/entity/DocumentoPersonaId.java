package com.proyecto.analisis.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoPersonaId implements Serializable {

    private Integer tipoDocumento;
    private Integer persona;
}
