package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Empresa;
import com.proyecto.analisis.entity.Genero;
import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.entity.Sucursal;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.repository.GeneroRepository;
import com.proyecto.analisis.repository.RoleRepository;
import com.proyecto.analisis.repository.StatusUsuarioRepository;
import com.proyecto.analisis.repository.SucursalRepository;
import com.proyecto.analisis.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private AuthService authService;

    public Usuario createUsuario(Usuario usuario, String token) {
        logger.debug("Creando usuario con datos: {}", usuario);
        if (usuario.getSucursal() == null) {
            logger.error("Sucursal es requerida para crear un usuario.");
            throw new IllegalArgumentException("Sucursal es requerida para crear un usuario.");
        }
        Sucursal sucursal = sucursalRepository.findById(usuario.getSucursal().getIdSucursal())
                .orElseThrow(() -> {
                    logger.error("Sucursal no encontrada con ID: {}", usuario.getSucursal().getIdSucursal());
                    return new IllegalArgumentException("Sucursal no encontrada con ID: " + usuario.getSucursal().getIdSucursal());
                });
        if (sucursal.getEmpresa() == null) {
            logger.error("Empresa es requerida a través de la sucursal.");
            throw new IllegalArgumentException("Empresa es requerida a través de la sucursal.");
        }
        Empresa empresa = sucursal.getEmpresa();

        validatePassword(usuario.getPassword(), empresa);
        usuario.setPassword(encryptMD5(usuario.getPassword()));

        LocalDateTime now = LocalDateTime.now();
        usuario.setFechaCreacion(now);
        usuario.setUsuarioCreacion(authService.getCurrentUserId(token));
        usuario.setUltimaFechaCambioPassword(now);
        usuario.setIntentosDeAcceso(0);
        usuario.setRequiereCambiarPassword(1);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        logger.info("Usuario creado exitosamente con ID: {}", savedUsuario.getIdUsuario());
        return savedUsuario;
    }

    public Usuario getUsuarioById(String id) {
        logger.debug("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
    }

    public List<Usuario> getAllUsuarios() {
        logger.debug("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario updateUsuario(String id, Usuario updatedUsuario, String token) {
        logger.debug("Actualizando usuario con ID: {} con datos: {}", id, updatedUsuario);
        Usuario existingUsuario = getUsuarioById(id);

        if (updatedUsuario.getNombre() != null) existingUsuario.setNombre(updatedUsuario.getNombre());
        if (updatedUsuario.getApellido() != null) existingUsuario.setApellido(updatedUsuario.getApellido());
        if (updatedUsuario.getFechaNacimiento() != null) existingUsuario.setFechaNacimiento(updatedUsuario.getFechaNacimiento());
        if (updatedUsuario.getStatusUsuario() != null) existingUsuario.setStatusUsuario(updatedUsuario.getStatusUsuario());
        if (updatedUsuario.getGenero() != null) existingUsuario.setGenero(updatedUsuario.getGenero());
        if (updatedUsuario.getUltimaFechaIngreso() != null) existingUsuario.setUltimaFechaIngreso(updatedUsuario.getUltimaFechaIngreso());
        if (updatedUsuario.getIntentosDeAcceso() != null) existingUsuario.setIntentosDeAcceso(updatedUsuario.getIntentosDeAcceso());
        if (updatedUsuario.getSesionActual() != null) existingUsuario.setSesionActual(updatedUsuario.getSesionActual());
        if (updatedUsuario.getUltimaFechaCambioPassword() != null) existingUsuario.setUltimaFechaCambioPassword(updatedUsuario.getUltimaFechaCambioPassword());
        if (updatedUsuario.getCorreoElectronico() != null) existingUsuario.setCorreoElectronico(updatedUsuario.getCorreoElectronico());
        if (updatedUsuario.getRequiereCambiarPassword() != null) existingUsuario.setRequiereCambiarPassword(updatedUsuario.getRequiereCambiarPassword());
        if (updatedUsuario.getFotografia() != null) existingUsuario.setFotografia(updatedUsuario.getFotografia());
        if (updatedUsuario.getTelefonoMovil() != null) existingUsuario.setTelefonoMovil(updatedUsuario.getTelefonoMovil());
        if (updatedUsuario.getSucursal() != null) existingUsuario.setSucursal(updatedUsuario.getSucursal());
        if (updatedUsuario.getPregunta() != null) existingUsuario.setPregunta(updatedUsuario.getPregunta());
        if (updatedUsuario.getRespuesta() != null) existingUsuario.setRespuesta(updatedUsuario.getRespuesta());
        if (updatedUsuario.getRole() != null) existingUsuario.setRole(updatedUsuario.getRole());

        LocalDateTime now = LocalDateTime.now();
        existingUsuario.setFechaModificacion(now);
        existingUsuario.setUsuarioModificacion(authService.getCurrentUserId(token));

        Usuario savedUsuario = usuarioRepository.save(existingUsuario);
        logger.info("Usuario actualizado exitosamente con ID: {}", id);
        return savedUsuario;
    }

    public void deleteUsuario(String id) {
        logger.debug("Eliminando usuario con ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            logger.error("Usuario no encontrado con ID: {}", id);
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    public Sucursal getSucursalById(Integer id) {
        logger.debug("Buscando sucursal con ID: {}", id);
        return sucursalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Sucursal no encontrada con ID: {}", id);
                    return new RuntimeException("Sucursal no encontrada con ID: " + id);
                });
    }

    public Role getRoleById(Integer id) {
        logger.debug("Buscando rol con ID: {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Rol no encontrado con ID: {}", id);
                    return new RuntimeException("Rol no encontrado con ID: " + id);
                });
    }

    public StatusUsuario getStatusUsuarioById(Integer id) {
        logger.debug("Buscando estado de usuario con ID: {}", id);
        return statusUsuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Estado de usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Estado de usuario no encontrado con ID: " + id);
                });
    }

    public Genero getGeneroById(Integer id) {
        logger.debug("Buscando género con ID: {}", id);
        return generoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Género no encontrado con ID: {}", id);
                    return new RuntimeException("Género no encontrado con ID: " + id);
                });
    }

    public List<Sucursal> getAllSucursales() {
        logger.debug("Obteniendo todas las sucursales");
        return sucursalRepository.findAll();
    }

    public List<Role> getAllRoles() {
        logger.debug("Obteniendo todos los roles");
        return roleRepository.findAll();
    }

    public List<StatusUsuario> getAllStatusUsuarios() {
        logger.debug("Obteniendo todos los estados de usuario");
        return statusUsuarioRepository.findAll();
    }

    public List<Genero> getAllGeneros() {
        logger.debug("Obteniendo todos los géneros");
        return generoRepository.findAll();
    }

    private void validatePassword(String password, Empresa empresa) {
        logger.debug("Validando contraseña: {} para empresa: {}", password, empresa.getIdEmpresa());
        if (password == null || password.isEmpty()) {
            logger.error("El password no puede ser nulo o vacío.");
            throw new IllegalArgumentException("El password no puede ser nulo o vacío.");
        }
        if (empresa == null) {
            logger.error("La entidad Empresa no puede ser nula.");
            throw new IllegalArgumentException("La entidad Empresa no puede ser nula.");
        }

        int passwordLargo = empresa.getPasswordLargo() != null ? empresa.getPasswordLargo() : 8;
        int mayusculasRequeridas = empresa.getPasswordCantidadMayusculas() != null ? empresa.getPasswordCantidadMayusculas() : 1;
        int minusculasRequeridas = empresa.getPasswordCantidadMinusculas() != null ? empresa.getPasswordCantidadMinusculas() : 1;
        int numerosRequeridos = empresa.getPasswordCantidadNumeros() != null ? empresa.getPasswordCantidadNumeros() : 1;
        int especialesRequeridos = empresa.getPasswordCantidadCaracteresEspeciales() != null ? empresa.getPasswordCantidadCaracteresEspeciales() : 1;

        if (password.length() < passwordLargo) {
            logger.error("El password debe tener al menos {} caracteres.", passwordLargo);
            throw new IllegalArgumentException("El password debe tener al menos " + passwordLargo + " caracteres.");
        }

        int mayusculas = 0;
        int minusculas = 0;
        int numeros = 0;
        int especiales = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) mayusculas++;
            else if (Character.isLowerCase(c)) minusculas++;
            else if (Character.isDigit(c)) numeros++;
            else especiales++;
        }

        if (mayusculas < mayusculasRequeridas) {
            logger.error("El password debe tener al menos {} mayúsculas.", mayusculasRequeridas);
            throw new IllegalArgumentException("El password debe tener al menos " + mayusculasRequeridas + " mayúsculas.");
        }
        if (minusculas < minusculasRequeridas) {
            logger.error("El password debe tener al menos {} minúsculas.", minusculasRequeridas);
            throw new IllegalArgumentException("El password debe tener al menos " + minusculasRequeridas + " minúsculas.");
        }
        if (numeros < numerosRequeridos) {
            logger.error("El password debe tener al menos {} números.", numerosRequeridos);
            throw new IllegalArgumentException("El password debe tener al menos " + numerosRequeridos + " números.");
        }
        if (especiales < especialesRequeridos) {
            logger.error("El password debe tener al menos {} caracteres especiales.", especialesRequeridos);
            throw new IllegalArgumentException("El password debe tener al menos " + especialesRequeridos + " caracteres especiales.");
        }
        logger.info("Contraseña validada exitosamente.");
    }

    private String encryptMD5(String password) {
        try {
            logger.debug("Encriptando contraseña con MD5: {}", password);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String encrypted = sb.toString();
            logger.info("Contraseña encriptada exitosamente.");
            return encrypted;
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            logger.error("Error al encriptar el password con MD5", e);
            throw new RuntimeException("Error al encriptar el password con MD5", e);
        }
    }
}