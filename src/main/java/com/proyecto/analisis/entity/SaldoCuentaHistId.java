package com.proyecto.analisis.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaldoCuentaHistId implements Serializable {

    private Integer anio;
    private Integer mes;
    private Integer idSaldoCuenta;
}
