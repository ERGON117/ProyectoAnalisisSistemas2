package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.ChangePasswordRequest;
import com.proyecto.analisis.dto.LoginRequest;
import com.proyecto.analisis.exception.AuthResponse;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.entity.BitacoraAcceso;
import com.proyecto.analisis.entity.TipoAcceso;
import com.proyecto.analisis.entity.StatusUsuario;
import com.proyecto.analisis.repository.UsuarioRepository;
import com.proyecto.analisis.repository.BitacoraAccesoRepository;
import com.proyecto.analisis.repository.TipoAccesoRepository;
import com.proyecto.analisis.repository.StatusUsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BitacoraAccesoRepository bitacoraAccesoRepository;
    private final TipoAccesoRepository tipoAccesoRepository;
    private final StatusUsuarioRepository statusUsuarioRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    private Key key;

    public Key getSigningKey() {
        if (key == null) {
            key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        return key;
    }

    public AuthResponse login(LoginRequest request, String ip, String userAgent,
                              String sistemaOperativo, String dispositivo, String browser) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            //registrarBitacora(null, ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_NO_ENCONTRADO");
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        Usuario usuario = usuarioOpt.get();
        StatusUsuario status = usuario.getStatusUsuario();

        // Verificar estado del usuario
        if ("Inactivo".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_INACTIVO");
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if ("Bloqueado por intentos de acceso".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_BLOQUEADO");
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario bloqueado por intentos de acceso")
                    .build();
        }

        // Validar contraseña
        String md5Password = md5(request.getPassword());

        if (!usuario.getPassword().equalsIgnoreCase(md5Password)) {
            // Incrementar intentos de acceso
            int intentos = usuario.getIntentosDeAcceso() + 1;
            usuario.setIntentosDeAcceso(intentos);

            // Bloquear usuario si alcanza 5 intentos
            if (intentos >= 5) {
                StatusUsuario bloqueado = statusUsuarioRepository.findByNombre("Bloqueado por intentos de acceso")
                        .orElseThrow(() -> new RuntimeException("Status 'Bloqueado' no encontrado"));
                usuario.setStatusUsuario(bloqueado);
            }

            usuarioRepository.save(usuario);
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "LOGIN_FALLIDO");

            String mensaje = intentos >= 5 ?
                    "Usuario bloqueado" : "Credenciales inválidas";

            return AuthResponse.builder()
                    .success(false)
                    .mensaje(mensaje)
                    .build();
        }

        // Login exitoso: resetear intentos
        usuario.setIntentosDeAcceso(0);
        usuarioRepository.save(usuario);

        // Generar token JWT
        String token = Jwts.builder()
                .setSubject(usuario.getIdUsuario())
                .claim("nombre", usuario.getNombre())
                .claim("correo", usuario.getCorreoElectronico())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();

        registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "LOGIN_EXITOSO");

        return AuthResponse.builder()
                .success(true)
                .mensaje("Login exitoso")
                .token(token)
                .build();
    }

    public AuthResponse logout(String token) {
        return AuthResponse.builder()
                .success(true)
                .mensaje("Logout exitoso para el token: " + token)
                .build();
    }

    private void registrarBitacora(String idUsuario, String ip, String userAgent,
                                   String sistemaOperativo, String dispositivo, String browser, String accion) {

        int tipoAccesoId;

        switch (accion) {
            case "USUARIO_NO_ENCONTRADO":
                tipoAccesoId = 4;
                break;
            case "LOGIN_FALLIDO":
                tipoAccesoId = 2;
                break;
            case "USUARIO_INACTIVO":
                tipoAccesoId = 3;
                break;
            case "USUARIO_BLOQUEADO":
                tipoAccesoId = 2;
                break;
            case "LOGIN_EXITOSO":
            default:
                tipoAccesoId = 1;
                break;
        }

        TipoAcceso tipoAcceso = tipoAccesoRepository.getReferenceById(tipoAccesoId);

        BitacoraAcceso bitacora = new BitacoraAcceso();
        bitacora.setIdUsuario(idUsuario);
        bitacora.setDireccionIp(ip);
        bitacora.setHttpUserAgent(userAgent);
        bitacora.setSistemaOperativo(sistemaOperativo);
        bitacora.setDispositivo(dispositivo);
        bitacora.setBrowser(browser);
        bitacora.setFechaAcceso(LocalDateTime.now());
        bitacora.setTipoAcceso(tipoAcceso);

        bitacoraAccesoRepository.save(bitacora);
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando MD5", e);
        }
    }

public Usuario getProfile(String token) {
    if (token.startsWith("Bearer ")) {
        token = token.substring(7);
    }

    Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

    String idUsuario = claims.getSubject();
    return usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}

public AuthResponse changePassword(String token, ChangePasswordRequest request) {
    if (token.startsWith("Bearer ")) {
        token = token.substring(7);
    }

    Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

    String idUsuario = claims.getSubject();
    Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Validar contraseña actual
    String md5Current = md5(request.getCurrentPassword());
    if (!usuario.getPassword().equalsIgnoreCase(md5Current)) {
        return AuthResponse.builder()
                .success(false)
                .mensaje("La contraseña actual no es correcta")
                .build();
    }

    // Guardar nueva contraseña
    usuario.setPassword(md5(request.getNewPassword()));
    usuarioRepository.save(usuario);

    return AuthResponse.builder()
            .success(true)
            .mensaje("Contraseña actualizada correctamente")
            .build();
}


}
