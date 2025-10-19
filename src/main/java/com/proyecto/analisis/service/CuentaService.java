package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.CreateCuentaDTO;
import com.proyecto.analisis.dto.CatologosCuentasDTO;
import com.proyecto.analisis.dto.SaldoCuentaResumenDTO;
import com.proyecto.analisis.entity.*;
import com.proyecto.analisis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private SaldoCuentaRepository saldoCuentaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private StatusCuentaRepository statusCuentaRepository;

    @Autowired
    private TipoSaldoCuentaRepository tipoSaldoCuentaRepository;

    @Autowired
    private DocumentoPersonaRepository documentoPersonaRepository;

    @Autowired
    private AuthService authService;

    public List<SaldoCuenta> getCuentasByPersona(Integer idPersona) {
        // Validar que la persona existe
        if (!personaRepository.existsById(idPersona)) {
            throw new IllegalArgumentException("Persona no encontrada con ID: " + idPersona);
        }
        return saldoCuentaRepository.findByPersonaIdPersona(idPersona);
    }

    @Transactional
    public SaldoCuenta createCuenta(CreateCuentaDTO dto, String token) {
        // Validar persona
        Persona persona = personaRepository.findById(dto.getIdPersona())
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        // Validar status cuenta
        StatusCuenta status = statusCuentaRepository.findById(dto.getIdStatusCuenta())
                .orElseThrow(() -> new IllegalArgumentException("Status de cuenta no encontrado"));

        // Validar tipo saldo cuenta
        TipoSaldoCuenta tipo = tipoSaldoCuentaRepository.findById(dto.getIdTipoSaldoCuenta())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de saldo cuenta no encontrado"));

        BigDecimal saldoAnterior = Optional.ofNullable(dto.getSaldoAnterior()).orElse(BigDecimal.ZERO);
        BigDecimal debitos = Optional.ofNullable(dto.getDebitos()).orElse(BigDecimal.ZERO);
        BigDecimal creditos = Optional.ofNullable(dto.getCreditos()).orElse(BigDecimal.ZERO);

        LocalDateTime now = LocalDateTime.now();

        SaldoCuenta cuenta = SaldoCuenta.builder()
                .persona(persona)
                .statusCuenta(status)
                .tipoSaldoCuenta(tipo)
                .saldoAnterior(saldoAnterior)
                .debitos(debitos)
                .creditos(creditos)
                .fechaCreacion(now)
                .usuarioCreacion(authService.getCurrentUserId(token))
                .build();

        return saldoCuentaRepository.save(cuenta);
    }

    @Transactional
    public SaldoCuenta updateCuenta(Integer id, CreateCuentaDTO dto, String token) {
        SaldoCuenta cuenta = saldoCuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        // Actualizar solo si los campos no son null
        if (dto.getIdPersona() != null) {
            Persona persona = personaRepository.findById(dto.getIdPersona())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
            cuenta.setPersona(persona);
        }

        if (dto.getIdStatusCuenta() != null) {
            StatusCuenta status = statusCuentaRepository.findById(dto.getIdStatusCuenta())
                    .orElseThrow(() -> new IllegalArgumentException("Status de cuenta no encontrado"));
            cuenta.setStatusCuenta(status);
        }

        if (dto.getIdTipoSaldoCuenta() != null) {
            TipoSaldoCuenta tipo = tipoSaldoCuentaRepository.findById(dto.getIdTipoSaldoCuenta())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de saldo cuenta no encontrado"));
            cuenta.setTipoSaldoCuenta(tipo);
        }

        if (dto.getSaldoAnterior() != null) {
            cuenta.setSaldoAnterior(dto.getSaldoAnterior());
        }

        if (dto.getDebitos() != null) {
            cuenta.setDebitos(dto.getDebitos());
        }

        if (dto.getCreditos() != null) {
            cuenta.setCreditos(dto.getCreditos());
        }

        LocalDateTime now = LocalDateTime.now();
        cuenta.setFechaModificacion(now);
        cuenta.setUsuarioModificacion(authService.getCurrentUserId(token));

        return saldoCuentaRepository.save(cuenta);
    }

    // NUEVO: Obtener todas las cuentas
    public List<SaldoCuenta> getAllCuentas() {
        return saldoCuentaRepository.findAll();
    }

    // NUEVO: Obtener cuenta por ID
    public Optional<SaldoCuenta> getCuentaById(Integer id) {
        return saldoCuentaRepository.findById(id);
    }

    // NUEVO: Eliminar cuenta
    @Transactional
    public void deleteCuenta(Integer id) {
        SaldoCuenta cuenta = saldoCuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        saldoCuentaRepository.delete(cuenta);
    }

    // CORREGIDO: Fórmula correcta SaldoActual = SaldoAnterior + Debitos - Creditos
    public BigDecimal getSaldoActual(Integer idCuenta) {
        SaldoCuenta cuenta = saldoCuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        
        // Fórmula CORREGIDA: SaldoAnterior + Debitos - Creditos
        return cuenta.getSaldoAnterior()
                .add(cuenta.getDebitos())  // Debitos se suman al saldo anterior
                .subtract(cuenta.getCreditos()); // Creditos se restan del saldo
    }

    public List<DocumentoPersona> getDocumentosByPersona(Integer idPersona) {
        return documentoPersonaRepository.findByPersonaIdPersona(idPersona);
    }

    // NUEVO: Obtener catálogos para el frontend
    public CatologosCuentasDTO getCatalogos() {
        return CatologosCuentasDTO.builder()
                .statusCuentas(statusCuentaRepository.findAll())
                .tiposSaldoCuentas(tipoSaldoCuentaRepository.findAll())
                .build();
    }

    // NUEVO: Verificar si persona ya tiene cuentas
    public boolean personaTieneCuentas(Integer idPersona) {
        return !saldoCuentaRepository.findByPersonaIdPersona(idPersona).isEmpty();
    }

    // NUEVO: Obtener resumen de cuentas por persona
    public List<SaldoCuentaResumenDTO> getResumenCuentasPorPersona(Integer idPersona) {
        List<SaldoCuenta> cuentas = saldoCuentaRepository.findByPersonaIdPersona(idPersona);
        return cuentas.stream().map(cuenta -> SaldoCuentaResumenDTO.builder()
                .idSaldoCuenta(cuenta.getIdSaldoCuenta())
                .statusCuentaNombre(cuenta.getStatusCuenta().getNombre())
                .tipoSaldoCuentaNombre(cuenta.getTipoSaldoCuenta().getNombre())
                .saldoActual(getSaldoActual(cuenta.getIdSaldoCuenta()))
                .build()).toList();
    }
}