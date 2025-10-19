package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.ChangePasswordRequest;
import com.proyecto.analisis.dto.PasswordResetRequest;
import com.proyecto.analisis.dto.LoginRequest;
import com.proyecto.analisis.exception.AuthResponse;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.entity.BitacoraAcceso;
import com.proyecto.analisis.entity.Empresa;
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
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

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

    public String getCurrentUserId(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token no proporcionado");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public AuthResponse login(LoginRequest request, String ip, String userAgent,
                              String sistemaOperativo, String dispositivo, String browser) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        Usuario usuario = usuarioOpt.get();
        StatusUsuario status = usuario.getStatusUsuario();

        // Verificar estado del usuario
        if ("Inactivo".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_INACTIVO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if ("Bloqueado por intentos de acceso".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_BLOQUEADO", null);
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
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "LOGIN_FALLIDO", null);

            String mensaje = intentos >= 5 ?
                    "Usuario bloqueado" : "Credenciales inválidas";

            return AuthResponse.builder()
                    .success(false)
                    .mensaje(mensaje)
                    .build();
        }

        // Verificar si se requiere cambio de contraseña
        boolean requiereCambio = usuario.getRequiereCambiarPassword() == 1;
        if (!requiereCambio && usuario.getUltimaFechaCambioPassword() != null) {
            Empresa empresa = usuario.getSucursal().getEmpresa();
            long diasDesdeCambio = ChronoUnit.DAYS.between(usuario.getUltimaFechaCambioPassword(), LocalDateTime.now());
            if (diasDesdeCambio > empresa.getPasswordCantidadCaducidadDias()) {
                requiereCambio = true;
            }
        }

        if (requiereCambio) {
            return AuthResponse.builder()
                    .success(true)
                    .mensaje("Se requiere cambio de contraseña")
                    .token(null)
                    .requiereCambioPassword(true)
                    .build();
        }

        // Login exitoso: resetear intentos
        usuario.setIntentosDeAcceso(0);
        usuario.setUltimaFechaIngreso(LocalDateTime.now());
        usuario.setSesionActual(md5(usuario.getIdUsuario()));
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

        registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "LOGIN_EXITOSO", token);

        return AuthResponse.builder()
                .success(true)
                .mensaje("Login exitoso")
                .token(token)
                .requiereCambioPassword(false)
                .build();
    }

    public AuthResponse logout(String token) {
        return AuthResponse.builder()
                .success(true)
                .mensaje("Logout exitoso para el token: " + token)
                .build();
    }

    private void registrarBitacora(String idUsuario, String ip, String userAgent,
                                   String sistemaOperativo, String dispositivo, String browser, String accion, String sesion) {

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
            case "CHANGE_PASSWORD_EXITOSO":
            case "CHANGE_PASSWORD_FALLIDO":
                tipoAccesoId = 5; // Nuevo tipo de acceso para cambio de contraseña
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
        bitacora.setAccion(accion);
        bitacora.setSesion(sesion != null ? sesion : "");

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

    public AuthResponse changePassword(ChangePasswordRequest request, String ip, String userAgent,
                                      String sistemaOperativo, String dispositivo, String browser) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico());

        if (usuarioOpt.isEmpty()) {
            //registrarBitacora(null, ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_NO_ENCONTRADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        Usuario usuario = usuarioOpt.get();
        StatusUsuario status = usuario.getStatusUsuario();

        if ("Inactivo".equalsIgnoreCase(status.getNombre())) {
           // registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_INACTIVO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if ("Bloqueado por intentos de acceso".equalsIgnoreCase(status.getNombre())) {
           // registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_BLOQUEADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario bloqueado por intentos de acceso")
                    .build();
        }

        // Validar contraseña actual
        String md5Current = md5(request.getCurrentPassword());
        if (!usuario.getPassword().equalsIgnoreCase(md5Current)) {
           // registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "CHANGE_PASSWORD_FALLIDO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("La contraseña actual no es correcta")
                    .build();
        }

        // Validar nueva contraseña contra las reglas de la empresa
        String validationError = validatePassword(request.getNewPassword(), usuario.getSucursal().getEmpresa());
        if (validationError != null) {
           // registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "CHANGE_PASSWORD_FALLIDO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje(validationError)
                    .build();
        }

        // Guardar nueva contraseña
        usuario.setPassword(md5(request.getNewPassword()));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0); // Resetear el flag
        usuario.setIntentosDeAcceso(0);
        usuario.setUsuarioModificacion(usuario.getIdUsuario());
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        //registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "CHANGE_PASSWORD_EXITOSO", null);

        return AuthResponse.builder()
                .success(true)
                .mensaje("Contraseña actualizada correctamente")
                .requiereCambioPassword(false)
                .build();
    }

    public AuthResponse resetPassword(PasswordResetRequest request, String ip, String userAgent,
                                     String sistemaOperativo, String dispositivo, String browser) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico());

        if (usuarioOpt.isEmpty()) {
            registrarBitacora(null, ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_NO_ENCONTRADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        Usuario usuario = usuarioOpt.get();
        StatusUsuario status = usuario.getStatusUsuario();

        if ("Inactivo".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_INACTIVO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if ("Bloqueado por intentos de acceso".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_BLOQUEADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario bloqueado por intentos de acceso")
                    .build();
        }

        if (!usuario.getRespuesta().equalsIgnoreCase(request.getRespuesta())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "PASSWORD_RESET_FALLIDO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Respuesta de seguridad incorrecta")
                    .build();
        }

        String validationError = validatePassword(request.getNewPassword(), usuario.getSucursal().getEmpresa());
        if (validationError != null) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "PASSWORD_RESET_FALLIDO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje(validationError)
                    .build();
        }

        usuario.setPassword(md5(request.getNewPassword()));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setIntentosDeAcceso(0);
        usuarioRepository.save(usuario);

        registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "PASSWORD_RESET_EXITOSO", null);

        return AuthResponse.builder()
                .success(true)
                .mensaje("Contraseña restablecida correctamente")
                .build();
    }

    private String validatePassword(String password, Empresa empresa) {
        if (password == null || password.isEmpty()) {
            return "La contraseña no puede estar vacía";
        }

        if (password.length() < empresa.getPasswordLargo()) {
            return "La contraseña debe tener al menos " + empresa.getPasswordLargo() + " caracteres";
        }

        int upperCount = 0, lowerCount = 0, digitCount = 0, specialCount = 0;
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) upperCount++;
            else if (Character.isLowerCase(c)) lowerCount++;
            else if (Character.isDigit(c)) digitCount++;
            else if (specialCharPattern.matcher(String.valueOf(c)).matches()) specialCount++;
        }

        if (upperCount < empresa.getPasswordCantidadMayusculas()) {
            return "La contraseña debe contener al menos " + empresa.getPasswordCantidadMayusculas() + " letras mayúsculas";
        }
        if (lowerCount < empresa.getPasswordCantidadMinusculas()) {
            return "La contraseña debe contener al menos " + empresa.getPasswordCantidadMinusculas() + " letras minúsculas";
        }
        if (digitCount < empresa.getPasswordCantidadNumeros()) {
            return "La contraseña debe contener al menos " + empresa.getPasswordCantidadNumeros() + " números";
        }
        if (specialCount < empresa.getPasswordCantidadCaracteresEspeciales()) {
            return "La contraseña debe contener al menos " + empresa.getPasswordCantidadCaracteresEspeciales() + " caracteres especiales";
        }

        return null;
    }

    public AuthResponse setSecurityQuestion(String token, String pregunta, String respuesta) {
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

        usuario.setPregunta(pregunta);
        usuario.setRespuesta(respuesta);
        usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .success(true)
                .mensaje("Pregunta y respuesta de seguridad actualizadas correctamente")
                .build();
    }

    public AuthResponse getSecurityQuestion(String correoElectronico, String ip, String userAgent,
                                           String sistemaOperativo, String dispositivo, String browser) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(correoElectronico);

        if (usuarioOpt.isEmpty()) {
            registrarBitacora(null, ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_NO_ENCONTRADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        Usuario usuario = usuarioOpt.get();
        StatusUsuario status = usuario.getStatusUsuario();

        if ("Inactivo".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_INACTIVO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if ("Bloqueado por intentos de acceso".equalsIgnoreCase(status.getNombre())) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "USUARIO_BLOQUEADO", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("Usuario bloqueado por intentos de acceso")
                    .build();
        }

        if (usuario.getPregunta() == null || usuario.getPregunta().isEmpty()) {
            registrarBitacora(usuario.getIdUsuario(), ip, userAgent, sistemaOperativo, dispositivo, browser, "PREGUNTA_NO_CONFIGURADA", null);
            return AuthResponse.builder()
                    .success(false)
                    .mensaje("No se ha configurado una pregunta de seguridad para este usuario")
                    .build();
        }

        return AuthResponse.builder()
                .success(true)
                .mensaje(usuario.getPregunta())
                .build();
    }
}