package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.EstadoCuentaDTO;
import com.proyecto.analisis.dto.MovimientoEstadoCuentaDTO;
import com.proyecto.analisis.entity.MovimientoCuenta;
import com.proyecto.analisis.entity.Persona;
import com.proyecto.analisis.entity.SaldoCuenta;
import com.proyecto.analisis.repository.MovimientoCuentaRepository;
import com.proyecto.analisis.repository.PersonaRepository;
import com.proyecto.analisis.repository.SaldoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCuentaService {

    private final MovimientoCuentaRepository movimientoRepo;
    private final SaldoCuentaRepository saldoCuentaRepo;
    private final PersonaRepository personaRepo;

    public EstadoCuentaDTO generarEstadoCuenta(
            Integer idPersona,
            Integer idSaldoCuenta,
            String nombre,
            String apellido,
            LocalDate inicio,
            LocalDate fin) {

        // 1️⃣ Buscar persona
        Persona persona = null;
        if (idPersona != null) {
            persona = personaRepo.findById(idPersona)
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        } else if (nombre != null && apellido != null) {
            persona = personaRepo.findFirstByNombreAndApellido(nombre, apellido)
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        }

        // 2️⃣ Buscar cuenta
        SaldoCuenta saldoCuenta;
        if (idSaldoCuenta != null) {
            saldoCuenta = saldoCuentaRepo.findById(idSaldoCuenta)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        } else if (persona != null) {
            saldoCuenta = saldoCuentaRepo.findFirstByPersona(persona)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        } else {
            throw new RuntimeException("Debe indicar idPersona, idSaldoCuenta o nombre/apellido");
        }

        // 3️⃣ Buscar movimientos del periodo
        var movimientos = movimientoRepo.findBySaldoCuentaAndFechaMovimientoBetweenOrderByFechaMovimientoAsc(
                saldoCuenta,
                inicio.atStartOfDay(),
                fin.atTime(23, 59, 59)
        );

        // 4️⃣ Calcular totales y saldo acumulado
        List<MovimientoEstadoCuentaDTO> lista = new ArrayList<>();
        BigDecimal saldoAcumulado = BigDecimal.ZERO;
        BigDecimal totalCargos = BigDecimal.ZERO;
        BigDecimal totalAbonos = BigDecimal.ZERO;

        for (MovimientoCuenta mov : movimientos) {
            BigDecimal cargo = BigDecimal.ZERO;
            BigDecimal abono = BigDecimal.ZERO;

            if (mov.getTipoMovimientoCXC().getOperacionCuentaCorriente() == 1) { // CARGO
                cargo = mov.getValorMovimiento();
                totalCargos = totalCargos.add(cargo);
                saldoAcumulado = saldoAcumulado.add(cargo);
            } else { // ABONO
                abono = mov.getValorMovimiento();
                totalAbonos = totalAbonos.add(abono);
                saldoAcumulado = saldoAcumulado.subtract(abono);
            }

            lista.add(MovimientoEstadoCuentaDTO.builder()
                    .fechaMovimiento(mov.getFechaMovimiento())
                    .tipoMovimiento(mov.getTipoMovimientoCXC().getNombre())
                    .descripcion(mov.getDescripcion())
                    .cargo(cargo)
                    .abono(abono)
                    .saldoAcumulado(saldoAcumulado)
                    .build());
        }

        // 5️⃣ Construir DTO de respuesta
        return EstadoCuentaDTO.builder()
                .nombreCliente(persona.getNombre() + " " + persona.getApellido())
                .numeroCuenta(saldoCuenta.getIdSaldoCuenta().toString())
                .periodo("Desde " + inicio + " hasta " + fin)
                .movimientos(lista)
                .totalCargos(totalCargos)
                .totalAbonos(totalAbonos)
                .saldoFinal(saldoAcumulado)
                .build();
    }
}
