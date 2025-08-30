package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    // Búsqueda por credenciales (si guardas el hash en password)
    Optional<Usuario> findByIdUsuarioAndPassword(String idUsuario, String password);

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByIdUsuario(String idUsuario);

    // Filtros por relaciones
    List<Usuario> findByRole_IdRole(Integer idRole);
    List<Usuario> findBySucursal_IdSucursal(Integer idSucursal);
    List<Usuario> findByStatusUsuario_Nombre(String nombreStatus);
    List<Usuario> findByGenero_Nombre(String nombreGenero);

    // Control de intentos / sesiones
    List<Usuario> findByUltimaFechaIngresoAfter(LocalDateTime fecha);
    List<Usuario> findBySesionActualIsNotNull();
}
