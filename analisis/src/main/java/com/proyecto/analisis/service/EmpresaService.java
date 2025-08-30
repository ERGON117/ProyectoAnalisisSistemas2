package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> obtenerPorId(Integer id) {
        return empresaRepository.findById(id);
    }

    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Empresa actualizar(Integer id, Empresa empresa) {
        return empresaRepository.findById(id)
                .map(e -> {
                    e.setNombre(empresa.getNombre());
                    e.setDireccion(empresa.getDireccion());
                    e.setNit(empresa.getNit());
                    e.setPasswordCantidadMayusculas(empresa.getPasswordCantidadMayusculas());
                    e.setPasswordCantidadMinusculas(empresa.getPasswordCantidadMinusculas());
                    e.setPasswordCantidadCaracteresEspeciales(empresa.getPasswordCantidadCaracteresEspeciales());
                    e.setPasswordCantidadCaducidadDias(empresa.getPasswordCantidadCaducidadDias());
                    e.setPasswordLargo(empresa.getPasswordLargo());
                    e.setPasswordIntentosAntesDeBloquear(empresa.getPasswordIntentosAntesDeBloquear());
                    e.setPasswordCantidadNumeros(empresa.getPasswordCantidadNumeros());
                    e.setPasswordCantidadPreguntasValidar(empresa.getPasswordCantidadPreguntasValidar());
                    e.setFechaModificacion(empresa.getFechaModificacion());
                    e.setUsuarioModificacion(empresa.getUsuarioModificacion());
                    return empresaRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        empresaRepository.deleteById(id);
    }
}

