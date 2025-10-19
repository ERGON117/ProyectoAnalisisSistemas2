package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    List<Sucursal> findByEmpresa_IdEmpresa(Integer idEmpresa);
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);
    List<Sucursal> findByEmpresa_IdEmpresaAndNombreContainingIgnoreCase(Integer idEmpresa, String nombre);
    boolean existsByEmpresa_IdEmpresaAndNombreIgnoreCase(Integer idEmpresa, String nombre);
}
