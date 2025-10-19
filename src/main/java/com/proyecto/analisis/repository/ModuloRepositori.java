package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuloRepositori extends JpaRepository<Modulo, Integer> {
    List<Modulo> findAllByOrderByOrdenMenu();
}