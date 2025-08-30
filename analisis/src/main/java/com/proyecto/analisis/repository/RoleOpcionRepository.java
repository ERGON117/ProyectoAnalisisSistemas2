package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.entity.RoleOpcionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleOpcionRepository extends JpaRepository<RoleOpcion, RoleOpcionId> {
    Optional<RoleOpcion> findByRoleIdRoleAndOpcionIdOpcion(Integer idRole, Integer idOpcion);
}