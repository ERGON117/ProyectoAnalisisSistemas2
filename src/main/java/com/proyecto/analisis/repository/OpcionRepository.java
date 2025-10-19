package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcionRepository extends JpaRepository<Opcion, Integer> {
    // List<Opcion> findByIdMenuOrderByOrdenMenu(int idMenu);
}