// Actualización del Service usando Specifications y métodos derivados

package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.PeriodoCierreMes;
import com.proyecto.analisis.entity.SaldoCuenta;
import com.proyecto.analisis.entity.SaldoCuentaHist;
import com.proyecto.analisis.repository.PeriodoCierreMesRepository;
import com.proyecto.analisis.repository.SaldoCuentaHistRepository;
import com.proyecto.analisis.repository.SaldoCuentaRepository;
import com.proyecto.analisis.repository.specification.SaldoCuentaSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CierreMesService {

    @Autowired
    private PeriodoCierreMesRepository periodoCierreMesRepository;

    @Autowired
    private SaldoCuentaRepository saldoCuentaRepository;

    @Autowired
    private SaldoCuentaHistRepository saldoCuentaHistRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public void cerrarMes(Integer anio, Integer mes, String token) {
        // Usar método derivado para buscar período pendiente
        PeriodoCierreMes periodo = periodoCierreMesRepository.findByAnioAndMes(anio, mes)
                .filter(p -> p.getFechaCierre() == null)
                .orElseThrow(() -> new RuntimeException(
                    "No se encontró período pendiente de cierre para el año " + anio + " y mes " + mes));

        // Obtener todos los saldos
        List<SaldoCuenta> saldos = saldoCuentaRepository.findAll(); // o con specification para activos
        LocalDateTime ahora = LocalDateTime.now();

        for (SaldoCuenta saldo : saldos) {
            // Verificar duplicados usando método derivado
            if (saldoCuentaHistRepository.existsByAnioAndMesAndIdSaldoCuenta(anio, mes, saldo.getIdSaldoCuenta())) {
                throw new RuntimeException("Ya existe registro histórico para la cuenta " + saldo.getIdSaldoCuenta());
            }

            // Crear copia histórica
            SaldoCuentaHist hist = SaldoCuentaHist.builder()
                    .anio(anio)
                    .mes(mes)
                    .idSaldoCuenta(saldo.getIdSaldoCuenta())
                    .persona(saldo.getPersona())
                    .statusCuenta(saldo.getStatusCuenta())
                    .tipoSaldoCuenta(saldo.getTipoSaldoCuenta())
                    .saldoAnterior(saldo.getSaldoAnterior())
                    .debitos(saldo.getDebitos())
                    .creditos(saldo.getCreditos())
                    .fechaCreacion(saldo.getFechaCreacion())
                    .usuarioCreacion(saldo.getUsuarioCreacion())
                    .fechaModificacion(ahora)
                    .usuarioModificacion(authService.getCurrentUserId(token))
                    .build();

            saldoCuentaHistRepository.save(hist);

            // Calcular y actualizar saldo actual
            BigDecimal saldoActual = saldo.getSaldoAnterior() != null ? saldo.getSaldoAnterior() : BigDecimal.ZERO;
            saldoActual = saldoActual.add(saldo.getDebitos() != null ? saldo.getDebitos() : BigDecimal.ZERO);
            saldoActual = saldoActual.subtract(saldo.getCreditos() != null ? saldo.getCreditos() : BigDecimal.ZERO);

            saldo.setSaldoAnterior(saldoActual);
            saldo.setDebitos(BigDecimal.ZERO);
            saldo.setCreditos(BigDecimal.ZERO);
            saldo.setFechaModificacion(ahora);
            saldo.setUsuarioModificacion(authService.getCurrentUserId(token));

            saldoCuentaRepository.save(saldo);
        }

        // Actualizar fecha de cierre
        periodo.setFechaCierre(ahora);
        periodoCierreMesRepository.save(periodo);
    }

    public List<PeriodoCierreMes> obtenerPeriodosPendientes() {
        // Usar método derivado directamente
        return periodoCierreMesRepository.findByFechaCierreIsNullOrderByAnioAscMesAsc();
    }

    // Método para obtener saldos activos usando Specification
    public List<SaldoCuenta> obtenerSaldosActivos() {
        return saldoCuentaRepository.findAll(SaldoCuentaSpecifications.isActivo());
    }
}