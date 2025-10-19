package com.proyecto.analisis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERIODO_CIERRE_MES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PeriodoCierreMesId.class)
public class PeriodoCierreMes {

    @Id
    @Column(name = "Anio")
    private Integer anio;

    @Id
    @Column(name = "Mes")
    private Integer mes;

    @Column(name = "FechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "FechaFinal")
    private LocalDate fechaFinal;

    @Column(name = "FechaCierre")
    private LocalDateTime fechaCierre;
}
