package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.SaldoConsultaDTO;
import com.proyecto.analisis.entity.Persona;
import com.proyecto.analisis.entity.SaldoCuenta;
import com.proyecto.analisis.repository.PersonaRepository;
import com.proyecto.analisis.repository.SaldoCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaldoCuentaService {

    @Autowired
    private SaldoCuentaRepository saldoCuentaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<SaldoConsultaDTO> consultarSaldos(Integer idPersona, Integer idSaldoCuenta, String nombre, String apellido) {
        List<SaldoCuenta> saldos = new ArrayList<>();

        if (idSaldoCuenta != null) {
            Optional<SaldoCuenta> optionalSaldo = saldoCuentaRepository.findById(idSaldoCuenta);
            optionalSaldo.ifPresent(saldos::add);
        } else if (idPersona != null) {
            saldos = saldoCuentaRepository.findByPersonaIdPersona(idPersona);
        } else if (StringUtils.hasText(nombre) && StringUtils.hasText(apellido)) {
            // Búsqueda case-insensitive manual
            List<Persona> personas = personaRepository.findAll().stream()
                    .filter(p -> p.getNombre() != null && p.getApellido() != null)
                    .filter(p -> p.getNombre().trim().equalsIgnoreCase(nombre.trim()) &&
                                p.getApellido().trim().equalsIgnoreCase(apellido.trim()))
                    .collect(Collectors.toList());
            
            for (Persona p : personas) {
                saldos.addAll(saldoCuentaRepository.findByPersona(p));
            }
        } else {
            return new ArrayList<>();
        }

        return mapToDTOs(saldos);
    }

    private List<SaldoConsultaDTO> mapToDTOs(List<SaldoCuenta> saldos) {
        List<SaldoConsultaDTO> dtos = new ArrayList<>();
        for (SaldoCuenta s : saldos) {
            SaldoConsultaDTO dto = new SaldoConsultaDTO();
            dto.setIdSaldoCuenta(s.getIdSaldoCuenta());
            
            Persona p = s.getPersona();
            if (p != null) {
                dto.setIdPersona(p.getIdPersona());
                dto.setNombrePersona(p.getNombre());
                dto.setApellidoPersona(p.getApellido());
            }
            
            if (s.getTipoSaldoCuenta() != null) {
                dto.setIdTipoSaldoCuenta(s.getTipoSaldoCuenta().getIdTipoSaldoCuenta());
                dto.setNombreTipo(s.getTipoSaldoCuenta().getNombre());
            }
            
            if (s.getStatusCuenta() != null) {
                dto.setIdStatusCuenta(s.getStatusCuenta().getIdStatusCuenta());
                dto.setNombreStatus(s.getStatusCuenta().getNombre());
            }
            
            dto.setSaldoInicial(s.getSaldoAnterior());
            dto.setCargos(s.getDebitos());
            dto.setAbonos(s.getCreditos());
            
            BigDecimal saldoFinal = BigDecimal.ZERO;
            if (s.getSaldoAnterior() != null) {
                saldoFinal = s.getSaldoAnterior();
            }
            if (s.getCreditos() != null) {
                saldoFinal = saldoFinal.add(s.getCreditos());
            }
            if (s.getDebitos() != null) {
                saldoFinal = saldoFinal.subtract(s.getDebitos());
            }
            dto.setSaldoFinal(saldoFinal);
            
            dtos.add(dto);
        }
        return dtos;
    }
}