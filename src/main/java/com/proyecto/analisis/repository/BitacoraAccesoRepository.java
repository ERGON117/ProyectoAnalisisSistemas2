package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.BitacoraAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BitacoraAccesoRepository extends JpaRepository<BitacoraAcceso, Integer> {

    // Últimos accesos de un usuario
    List<BitacoraAcceso> findTop10ByIdUsuarioOrderByFechaAccesoDesc(String idUsuario);

    // Conteo de intentos recientes (útil para bloqueo por intentos fallidos)
    long countByIdUsuarioAndFechaAccesoAfter(String idUsuario, LocalDateTime desde);

    // Por rango de fechas
    List<BitacoraAcceso> findByIdUsuarioAndFechaAccesoBetween(String idUsuario, LocalDateTime desde, LocalDateTime hasta);

    // Filtrar por tipo de acceso (ej. "Acceso Concedido", "Usuario Inactivo", etc.)
    List<BitacoraAcceso> findByTipoAcceso_NombreAndFechaAccesoBetween(String nombreTipo, LocalDateTime desde, LocalDateTime hasta);
}
