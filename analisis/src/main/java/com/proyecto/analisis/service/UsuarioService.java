package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Optional<Usuario> buscarPorCredenciales(String idUsuario, String password) {
        return repository.findByIdUsuarioAndPassword(idUsuario, password);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repository.findByCorreoElectronico(correo);
    }

    public boolean existePorCorreo(String correo) {
        return repository.existsByCorreoElectronico(correo);
    }

    public boolean existePorId(String idUsuario) {
        return repository.existsByIdUsuario(idUsuario);
    }

    public List<Usuario> listarPorRol(Integer idRole) {
        return repository.findByRole_IdRole(idRole);
    }

    public List<Usuario> listarPorSucursal(Integer idSucursal) {
        return repository.findBySucursal_IdSucursal(idSucursal);
    }

    public List<Usuario> listarPorStatus(String status) {
        return repository.findByStatusUsuario_Nombre(status);
    }

    public List<Usuario> listarPorGenero(String genero) {
        return repository.findByGenero_Nombre(genero);
    }

    public List<Usuario> listarConIngresosRecientes(LocalDateTime fecha) {
        return repository.findByUltimaFechaIngresoAfter(fecha);
    }

    public List<Usuario> listarConSesionActiva() {
        return repository.findBySesionActualIsNotNull();
    }
}
