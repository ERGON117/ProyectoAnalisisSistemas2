package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;
    private final AuthService authService;

    public EmpresaService(EmpresaRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public Empresa guardar(Empresa empresa, String token) {
        empresa.setFechaCreacion(LocalDateTime.now());
        empresa.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(empresa);
    }

    public List<Empresa> listarTodos() {
        return repository.findAll();
    }

    public Optional<Empresa> buscarPorId(Integer id) {
        return repository.findById(id);
    }



    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Empresa> actualizar(Integer id, Empresa empresaActualizada, String token) {
        return repository.findById(id).map(empresaExistente -> {
            empresaExistente.setNombre(empresaActualizada.getNombre());
            empresaExistente.setDireccion(empresaActualizada.getDireccion());
            empresaExistente.setNit(empresaActualizada.getNit());

            empresaExistente.setPasswordCantidadMayusculas(empresaActualizada.getPasswordCantidadMayusculas());
            empresaExistente.setPasswordCantidadMinusculas(empresaActualizada.getPasswordCantidadMinusculas());
            empresaExistente.setPasswordCantidadCaracteresEspeciales(empresaActualizada.getPasswordCantidadCaracteresEspeciales());
            empresaExistente.setPasswordCantidadCaducidadDias(empresaActualizada.getPasswordCantidadCaducidadDias());
            empresaExistente.setPasswordLargo(empresaActualizada.getPasswordLargo());
            empresaExistente.setPasswordIntentosAntesDeBloquear(empresaActualizada.getPasswordIntentosAntesDeBloquear());
            empresaExistente.setPasswordCantidadNumeros(empresaActualizada.getPasswordCantidadNumeros());
            empresaExistente.setPasswordCantidadPreguntasValidar(empresaActualizada.getPasswordCantidadPreguntasValidar());

            empresaExistente.setFechaModificacion(LocalDateTime.now());
            empresaExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(empresaExistente);
        });
    }
}
