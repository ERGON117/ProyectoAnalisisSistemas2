package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.MovimientoCuenta;
import com.proyecto.analisis.entity.SaldoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoCuentaRepository extends JpaRepository<MovimientoCuenta, Integer> {

    // Busca movimientos por cuenta ordenados por fecha
    List<MovimientoCuenta> findBySaldoCuentaOrderByFechaMovimientoAsc(SaldoCuenta saldoCuenta);

    // También podemos usar este si queremos filtrar por fechas
    List<MovimientoCuenta> findBySaldoCuentaAndFechaMovimientoBetweenOrderByFechaMovimientoAsc(
            SaldoCuenta saldoCuenta,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}
